package net.studio20design.tuneserver;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.CoreContainer;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * User: amir
 * Date: Apr 19, 2009
 * Time: 2:30:35 PM
 */
public class MusicDatabase {

    private static SolrServer server;

    private MusicDatabase() {
    }


    public static void addSongs(List<File> songs) throws IOException, ReadOnlyFileException, TagException, CannotReadException, SolrServerException {
        if (songs.size() > 0) {
            // todo problem if all files are in same dir
            IndexerStatus.getInstance().setIndexing(true);
            IndexerStatus.getInstance().setTotalToIndex(songs.size());
            Map<File, List<File>> folders = new HashMap<File, List<File>>();
            log.info("Splitting files by parent dir...");
            for (File song : songs) {
                if (folders.get(song.getParentFile()) == null) {
                    folders.put(song.getParentFile(), new ArrayList<File>());
                }
                folders.get(song.getParentFile()).add(song);
            }

            int i = 0;
            Connection connection = null;
            PreparedStatement statement = null;
            try {
                connection = Derby.getConnection();
                statement = connection.prepareStatement("INSERT INTO songs (song_id, album_id, artist_id, artist, title, album, file, song_length) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                for (List<File> folder : folders.values()) {
                    List<SolrInputDocument> documents = new ArrayList<SolrInputDocument>();
                    for (File file : folder) {
                        log.finest("Reading ID3 tag for " + file);
                        SolrInputDocument document = new SolrInputDocument();
                        MP3File mp3File;
                        try {
                            mp3File = (MP3File) AudioFileIO.read(file);
                        } catch (InvalidAudioFrameException e) {
                            e.printStackTrace();
                            continue;
                        }
                        Tag tag = mp3File.getTag();
                        Song song = new Song(file.hashCode(), tag.getFirstArtist(), tag.getFirstAlbum(), tag.getFirstTitle(), mp3File.getMP3AudioHeader().getTrackLengthAsString(), file);
                        document.addField("filename", file.getName());
                        document.addField("title", song.getTitle());
                        document.addField("artist", song.getArtist());
                        document.addField("album", song.getAlbum());
                        document.addField("fullpath", file.getAbsolutePath());
                        document.addField("tracklength", song.getLength());
                        document.addField("id", song.getSongId());

                        statement.clearParameters();
                        statement.setLong(1, song.getSongId());
                        statement.setLong(2, song.getAlbumId());
                        statement.setLong(3, song.getArtistId());
                        statement.setString(4, song.getArtist());
                        statement.setString(5, song.getTitle());
                        statement.setString(6, song.getAlbum());
                        statement.setString(7, song.getFile().getAbsolutePath());
                        statement.setString(8, song.getLength());
                        statement.addBatch();

                        documents.add(document);
                        IndexerStatus.getInstance().getSongs().add(song);
                    }
                    statement.executeBatch();
                    server.add(documents);
                    log.info(String.format("Committing %d songs...", documents.size()));
                    server.commit();
                    log.info(String.format("Folders remaining: %d", (folders.size() - ++i)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            server.optimize();
            IndexerStatus.getInstance().complete();
            log.info(String.format("Indexing complete for %d files...", songs.size()));
        }
    }

    public static List<Song> searchRaw(String query, int limit) {
        List<Song> songs = new ArrayList<Song>();
        try {
            SolrQuery solrQuery = new SolrQuery(query);
            solrQuery.setRows(limit);
            QueryResponse queryResponse = server.query(solrQuery);
            SolrDocumentList solrDocumentList = queryResponse.getResults();

            for (SolrDocument doc : solrDocumentList) {
                songs.add(new Song(doc));
            }

        } catch (SolrServerException e) {
            e.printStackTrace();
        }

        return songs;
    }

    public static List<Song> searchRaw(String query) {
        return searchRaw(query, 20);
    }

    public static List<Song> search(String q) {
        String query = String.format("\"%1$s\"^10 OR \"%1$s*\"^8 OR (%1$s)^6 OR %1$s", q);
        return searchRaw(query);
    }

    public static List<Song> searchFuzzy(String q) {
        String query = String.format("\"%1$s\"^10 OR \"%1$s*\"^8 OR (%1$s)^6 OR (%1$s~)", q);
        return searchRaw(query);
    }

    public static List<Song> getSongs() {
        List<Song> songs = new ArrayList<Song>();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Derby.getConnection();
            statement = connection.prepareStatement("SELECT * FROM songs");
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                Song song = new Song(set.getLong("song_id"), set.getString("artist"), set.getString("album"), set.getString("title"), set.getString("song_length"), new File(set.getString("file")));
                songs.add(song);
            }
            set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return songs;

    }

    public static Artist getArtist(long id) {
        Artist artist = null;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Derby.getConnection();
            statement = connection.prepareStatement("SELECT * FROM songs WHERE artist_id=?");
            statement.setLong(1, id);
            ResultSet set = statement.executeQuery();

            Map<Long, Album> map = new HashMap<Long, Album>();
            while (set.next()) {
                if (!map.containsKey(set.getLong("album_id"))) {
                    map.put(set.getLong("album_id"), new Album(set.getString("artist"), set.getString("album"), set.getLong("album_id")));
                }

                map.get(set.getLong("album_id")).getSongs().add(new Song(set.getLong("song_id"), set.getString("artist"), set.getString("album"), set.getString("title"), set.getString("song_length"), new File(set.getString("file"))));
            }
            set.close();
            artist = new Artist(map.values());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return artist;
    }

    public static List<Album> getAlbums() {
        List<Album> albums = new ArrayList<Album>();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Derby.getConnection();
            statement = connection.prepareStatement("SELECT MAX(artist) as artist, MAX(album) as album, album_id FROM songs GROUP BY album_id ORDER BY artist");
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                albums.add(new Album(set.getString("artist"), set.getString("album"), Long.parseLong(set.getString("album_id"))));
            }
            set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return albums;

    }

    public static Album getAlbum(long albumId) {
        List<Song> album = new ArrayList<Song>();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Derby.getConnection();
            statement = connection.prepareStatement("SELECT * FROM songs WHERE album_id = ?");
            statement.setLong(1, albumId);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                Song song = new Song(set.getLong("song_id"), set.getString("artist"), set.getString("album"), set.getString("title"), set.getString("song_length"), new File(set.getString("file")));
                album.add(song);
            }
            set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return new Album(album);
    }

    public static Song getSong(long id) {
        Song song = null;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Derby.getConnection();
            statement = connection.prepareStatement("SELECT * FROM songs WHERE song_id = ?");
            statement.setLong(1, id);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                song = new Song(id, set.getString("artist"), set.getString("album"), set.getString("title"), set.getString("song_length"), new File(set.getString("file")));
            }
            set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return song;
    }

    private static Logger log;

    static {
        try {
            server = new EmbeddedSolrServer(new CoreContainer(MusicDatabase.class.getClassLoader().getResource("solr").getPath().replaceAll("%20", " "), new File(MusicDatabase.class.getClassLoader().getResource("solr/solr.xml").getPath().replaceAll("%20", " "))), "core0");
            log = LogUtils.getLogger(MusicDatabase.class);
            Connection conn = null;
            Statement statement = null;
            try {
                conn = Derby.getConnection();
                statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM songs");
                resultSet.close();
            } catch (SQLException e) {
                try {
                    Statement createStatement = conn.createStatement();
                    createStatement.execute("CREATE TABLE songs(song_id BIGINT PRIMARY KEY, album_id BIGINT, artist_id BIGINT, artist VARCHAR(255), title VARCHAR(255), album VARCHAR(255), file VARCHAR(255), song_length VARCHAR(10))");
                    createStatement.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } finally {
                try {
                    if (statement != null) {
                        statement.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
