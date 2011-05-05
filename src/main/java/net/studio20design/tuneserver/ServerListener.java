package net.studio20design.tuneserver;
/**
 * User: amir
 * Date: May 17, 2009
 * Time: 11:43:30 AM
 */

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServerListener implements ServletContextListener {


    public ServerListener() {
    }


    public void contextInitialized(ServletContextEvent sce) {
        System.setProperty("tuneserver.home.dir", sce.getServletContext().getInitParameter("tuneserver.home.dir") + "/.tuneserver");


        Connection conn = null;
        PreparedStatement statement = null;
        File dir = null;
        try {
            conn = Derby.getConnection();
            statement = conn.prepareStatement("SELECT path FROM indexerStatus");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                dir = new File(resultSet.getString("path"));
            }
            resultSet.close();
        } catch (SQLException e) {
            //
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
        if (dir != null) {
            DirectoryListener listener = new DirectoryListener(dir);
            sce.getServletContext().setAttribute("listener", listener);
            sce.getServletContext().setAttribute("firstTime", false);
        } else {
            sce.getServletContext().setAttribute("firstTime", true);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
}
