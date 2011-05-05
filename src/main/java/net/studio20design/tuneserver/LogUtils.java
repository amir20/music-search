package net.studio20design.tuneserver;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * User: amir
 * Date: Jun 10, 2009
 * Time: 7:42:23 PM
 */
public class LogUtils {
    private static Logger log = Logger.getLogger("");
    static{
        File f = new File(System.getProperty("tuneserver.home.dir") + "/tuneserver.log");
        if (!f.exists()) {
            f.getParentFile().mkdirs();
        }
        FileHandler fh;
        try {
            fh = new FileHandler(System.getProperty("tuneserver.home.dir") + "/tuneserver.log");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fh.setFormatter( new SimpleFormatter());
        log.addHandler(fh);

    }
    public static Logger getLogger(Class clazz) throws IOException {
        return Logger.getLogger(clazz.toString());
    }
}