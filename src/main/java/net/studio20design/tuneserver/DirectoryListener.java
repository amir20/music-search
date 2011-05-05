package net.studio20design.tuneserver;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: amir
 * Date: Apr 19, 2009
 * Time: 2:31:29 PM
 */
public class DirectoryListener {
    private final File dir;
    private Timer timer;
    private static Logger log;
    private Object notifier = null;

    static {
        try {
            log = LogUtils.getLogger(DirectoryListener.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public DirectoryListener(File dir) {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Not a valid directory: " + dir);
        }
        this.dir = dir;
        init();
    }

    public void setNotifier(Object notifier) {
        this.notifier = notifier;
    }

    private void init() {
        timer = new Timer("Mp3 Directory Listner");

        TimerTask timerTask = new TimerTask() {
            public void run() {
                final long lastIndexed = getLastIndexedTimestamp();
                FileFilter fileFilter = new FileFilter() {
                    public boolean accept(File file) {
                        return file.isDirectory() || (file.lastModified() > lastIndexed && file.getName().endsWith(".mp3"));
                    }
                };
                List<File> songs = findMusic(dir, fileFilter);
                log.info(String.format("Found %d songs", songs.size()));
                try {
                    long cm = System.currentTimeMillis();
                    MusicDatabase.addSongs(songs);
                    updateLastIndexed(cm);
                } catch (Exception e) {
                    log.log(Level.SEVERE, "Error while indexing songs", e);
                    e.printStackTrace();
                }
                if (notifier != null) {
                    synchronized (notifier) {
                        notifier.notify();
                    }
                }
            }
        };
        timer.schedule(timerTask, 0, 12 * 60 * 60 * 1000);
    }

    private List<File> findMusic(File dir, FileFilter fileFilter) {
        log.info("Looking for songs in " + dir);
        List<File> newFiles = new ArrayList<File>();
        File[] files = dir.listFiles(fileFilter);
        for (File file : files) {
            if (file.isDirectory()) {
                newFiles.addAll(findMusic(file, fileFilter));
            } else {
                newFiles.add(file);
            }
        }
        return newFiles;
    }


    private long getLastIndexedTimestamp() {
        Connection conn = null;
        PreparedStatement statement = null;
        long res = 0;
        try {
            conn = Derby.getConnection();
            statement = conn.prepareStatement("SELECT time_stamp FROM indexerStatus WHERE id = ?");
            statement.setInt(1, dir.hashCode());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                res = resultSet.getLong(1);
            }
            resultSet.close();
        } catch (SQLException e) {
            try {
                Statement createStatement = conn.createStatement();
                createStatement.execute("CREATE TABLE indexerStatus(id BIGINT PRIMARY KEY, time_stamp BIGINT, path VARCHAR(255)) ");
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
        return res;
    }

    private void updateLastIndexed(long tm) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = Derby.getConnection();
            statement = conn.prepareStatement("INSERT INTO indexerStatus (id, time_stamp, path) VALUES (?,?,?)");
            statement.setInt(1, dir.hashCode());
            statement.setLong(2, tm);
            statement.setString(3, dir.getAbsolutePath());
            statement.executeUpdate();
        } catch (SQLException e) {
            try {
                if (statement != null) {
                    statement.close();
                }
                statement = conn.prepareStatement("UPDATE indexerStatus SET time_stamp = ? WHERE id = ?");
                statement.setLong(1, tm);
                statement.setInt(2, dir.hashCode());
                statement.executeUpdate();
            } catch (SQLException e2) {
                e2.printStackTrace();
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
    }

    public void stop() {
        timer.cancel();
        timer = null;
    }

}
