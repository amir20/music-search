package net.studio20design.tuneserver;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: amir
 * Date: May 20, 2009
 * Time: 10:44:07 PM
 */
public class Album {
    final List<Song> songs;
    final String artist;
    final String title;
    final long albumId;

    public Album(List<Song> songs) {
        this.songs = songs;
        artist = songs.get(0).getArtist();
        title = songs.get(0).getAlbum();
        albumId = songs.get(0).getAlbumId();
    }

    public Album(String artist, String title, long albumId) {
        songs = new ArrayList<Song>();
        this.artist = artist;
        this.title = title;
        this.albumId = albumId;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public long getAlbumId() {
        return albumId;
    }

    public Artwork getArtwork() throws InvalidAudioFrameException, IOException, ReadOnlyFileException, TagException, CannotReadException {
        for (Song song : songs) {
            MP3File mp3File = (MP3File) AudioFileIO.read(song.getFile());
            AbstractID3v2Tag tag = mp3File.getID3v2Tag();
            if (tag != null) {
                Artwork artwork = tag.getFirstArtwork();
                if (artwork != null) {
                    return artwork;
                }
            }
        }
        return null;
    }

    public Song getFirstSong() {
        return songs.get(0);
    }

    @Override
    public String toString() {
        return "Album{" +
                "artist='" + artist + '\'' +
                ", title='" + title + '\'' +
                ", songs=" + songs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Album album = (Album) o;

        if (albumId != album.albumId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (albumId ^ (albumId >>> 32));
    }
}
