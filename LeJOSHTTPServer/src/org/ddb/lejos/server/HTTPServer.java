package org.ddb.lejos.server;

import org.ddb.lejos.droid.Droid;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class HTTPServer {

	public static void main(String[] args) throws Exception{

        Server server = new Server(8080);
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
 
        context.addServlet(new ServletHolder(new DistanceHandler()),"/distance");
        context.addServlet(new ServletHolder(new PhotoHandler()),"/photo");
        context.addServlet(new ServletHolder(new GoToWallHandler()),"/gotowall");
        context.addServlet(new ServletHolder(new TurnHandler()),"/turn");
        context.addServlet(new ServletHolder(new RadarHandler()),"/radar");
        context.addServlet(new ServletHolder(new MoveHandler()),"/move");
        context.addServlet(new ServletHolder(new StatusHandler()),"/status");
        
        Droid.GetDroid();
        server.start();
        server.join();

	}

}
