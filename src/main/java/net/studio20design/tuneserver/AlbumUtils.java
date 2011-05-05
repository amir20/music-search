package net.studio20design.tuneserver;

import net.studio20design.fileutils.FutureZipFile;
import net.studio20design.fileutils.ZipUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlbumUtils {


    public static FutureZipFile zipAlbum(File zipFile, Album album) throws IOException {
        List<File> files = new ArrayList<File>();
        for (Song song : album.getSongs()) {
            files.add(song.getFile());
        }
        return ZipUtils.zipFiles(zipFile, files);
    }


}