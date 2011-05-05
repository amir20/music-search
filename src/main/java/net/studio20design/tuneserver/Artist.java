package net.studio20design.tuneserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: amir
 * Date: May 27, 2009
 * Time: 6:22:04 PM
 */
public class Artist {
    final private List<Album> albums;
    final private String name;

    public Artist(Collection<Album> albums) {
        this.albums = new ArrayList<Album>(albums);
        name = this.albums.get(0).getArtist();
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public String getName() {
        return name;
    }
}
