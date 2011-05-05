package net.studio20design.tuneserver;

import junit.framework.TestCase;
import net.studio20design.imageutils.Image;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User: amir
 * Date: Apr 19, 2009
 * Time: 2:48:00 PM
 */
public class TestSolrServer extends TestCase {
    public void setUp() throws IOException {
        System.setProperty("tuneserver.home.dir", new File(this.getClass().getClassLoader().getResource("").getFile() + "/.tuneserver").getCanonicalPath());
    }

    public void testSolr() throws InterruptedException {
        DirectoryListener listener = new DirectoryListener(new File("./music"));
        listener.setNotifier(this);
        synchronized (this) {
            wait();
        }
        IndexerStatus.getInstance().getPercentComlete();
        listener.stop();
    }

    public void testBeyonce() {
        List<Song> songs = MusicDatabase.searchFuzzy("beyonce");
        assertTrue("Beyonce should return one match", songs.size() == 1);
    }

    public void testAlbum() {
        List<Song> songs = MusicDatabase.search("album:sasha");
        assertTrue("Beyonce should have one album", songs.size() == 1);
    }

    public void testSongName() {
        List<Song> songs = MusicDatabase.search("single");
        assertTrue("Song 'single' should have one at least match", songs.size() == 1);
        System.out.println(songs);
    }

    public void testSongId() {
        Song song = MusicDatabase.getSong(MusicDatabase.searchFuzzy("beyonce").get(0).getSongId());
        assertNotNull("Searching by id should return music ", song);
    }

    public void testAlbumId() {
        Album album = MusicDatabase.getAlbum(MusicDatabase.searchFuzzy("beyonce").get(0).getAlbumId());
        assertTrue("Searching album by id should return one song", album.getSongs().size() > 0);
    }

    public void testAlbums() {
        List<Album> albums = MusicDatabase.getAlbums();
        assertTrue("There must be at least one album", albums.size() == 1);
    }

    public void testSongs() {
        assertTrue("There must be at least one song", MusicDatabase.getSongs().size() > 0);
    }

    public void testAlbumArt() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException {
        long id = MusicDatabase.searchFuzzy("beyonce").get(0).getAlbumId();
        Image image = AlbumArt.getAlbumArt(id, 48, 48, null);
        File cache = new File(System.getProperty("tuneserver.home.dir") + "/artworks", String.valueOf(id) + ".jpg");
        assertNotNull(image);
        assertTrue("File must exist", cache.exists());
    }
}
