package net.studio20design.tuneserver;

import junit.framework.TestCase;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * @author: Amir Raminfar
 * @version: 1.0
 * Date:        May 11, 2009
 * Time:        7:57:26 PM
 * **************************************************************************<br/>
 */
public class TestEmbeddedServer extends TestCase {
    public void test() throws Exception {
        //main(new String[]{null});
    }
    public static void main( String... args ) throws Exception {
        Server server = new Server();
        Connector connector = new SelectChannelConnector();
        connector.setPort(8080);
        //connector.setHost("127.0.0.1");
        server.addConnector(connector);

        WebAppContext wac = new WebAppContext();
        wac.setContextPath("/");
        wac.setWar("/Users/Amir/sandbox/java-projects/tuneserver/_project/tuneserver-1.0-SNAPSHOT.war");
        server.setHandler(wac);
        server.setStopAtShutdown(true);

        server.start();
        server.join();
    }
}
