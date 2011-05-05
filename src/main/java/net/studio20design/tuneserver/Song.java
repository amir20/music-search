package net.studio20design.tuneserver;

import org.apache.solr.common.SolrDocument;

import java.io.File;

/**
 * @author: Amir Raminfar
 * @version: 1.0
 * Date:        Apr 22, 2009
 * Time:        1:59:38 PM
 * **************************************************************************<br/>
 */
public class Song {
    private String title;
    private String artist;
    private String album;
    private String length;
    private long id;
    private File file;

    public Song(long id, String artist, String album, String title, String length, File file) {
        this.id = id;
        this.artist = artist;
        this.album = album;
        this.title = title;
        this.length = length;
        this.file = file;
    }

    public Song(SolrDocument document) {
        this(Long.parseLong(document.getFieldValue("id").toString()), document.getFieldValue("artist").toString(), document.getFieldValue("album").toString(), document.getFieldValue("title").toString(), document.getFieldValue("tracklength").toString(), new File(document.getFieldValue("fullpath").toString()));
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getLength() {
        return length;
    }

    public long getSongId() {
        return id;
    }

    public File getFile() {
        return file;
    }

    public long getAlbumId() {
        return (getArtist() + getAlbum()).hashCode();
    }

    public long getArtistId() {
        return getArtist().hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Song");
        sb.append("{id=").append(id);
        sb.append(", artist='").append(artist).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", length='").append(length).append('\'');
        sb.append(", albumId='").append(getAlbumId()).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Song song = (Song) o;

        return id == song.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
