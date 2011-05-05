package net.studio20design.tuneserver;

import net.studio20design.imageutils.Image;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * User: amir
 * Date: May 25, 2009
 * Time: 2:35:44 PM
 */
public class AlbumArt {
    public static Image getAlbumArt(long id, int width, int height, File defaultImage) throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException {
        Image image = null;
        File cache = new File(System.getProperty("tuneserver.home.dir") + "/artworks", String.valueOf(id) + ".jpg");
        if (cache.exists()) {
            image = new Image(cache);
        }
        if (image == null) {
            Album album = MusicDatabase.getAlbum(id);
            if (album.getArtwork() != null) {
                BufferedImage bufferedImage = album.getArtwork().getImage();
                image = new Image(bufferedImage);
            } else {
                image = new Image(defaultImage);
            }
            image.saveToDisk(cache);
        }
        image.bestFitTo(width, height);
        return image;
    }
}
