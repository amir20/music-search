package net.studio20design.tuneserver;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: amir
 * Date: Apr 20, 2009
 * Time: 7:41:29 PM
 */
public class Derby {
    private static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private static String protocol = "jdbc:derby:";

    private Derby() {
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(protocol + "data;create=true");
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new IllegalStateException("Could not get get a valid derby connection", e);
        }
    }

    private static Logger log;

    static {
        try {
            System.setProperty("derby.system.home", System.getProperty("tuneserver.home.dir"));
            Class.forName(driver).newInstance();
            log = LogUtils.getLogger(Derby.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

}
