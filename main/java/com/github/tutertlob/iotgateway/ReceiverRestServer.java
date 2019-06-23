package com.github.tutertlob.iotgateway;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;
import java.lang.Package;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class ReceiverRestServer {

	private static final Logger logger = Logger.getLogger(ReceiverRestServer.class.getName());

	private static HttpServer server;

	private static String base_uri;

	private static String getBaseUri() {
		String host;
		try {
			host = AppProperties.getInstance().getProperty("rest.host");
		} catch (IllegalArgumentException | NullPointerException e) {
			host = "localhost";
		}

		String port;
		try {
			port = AppProperties.getInstance().getProperty("rest.port");
		} catch (IllegalArgumentException | NullPointerException e) {
			port = "49152";
		}

		return String.format("http://%s:%s/receiver", host, port);
	}

	/**
	 * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
	 * application.
	 * 
	 * @return Grizzly HTTP server.
	 */
	private static HttpServer startServer(Package resource) {
		// create a resource config that scans for JAX-RS resources and providers
		// in the iot_applications package
		final ResourceConfig rc = new ResourceConfig().packages(resource.getName());

		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(base_uri), rc);
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void startRestServer(Package resource) throws IOException {
		logger.info("Starting ReceiverRestServer...");
		base_uri = getBaseUri();
		server = startServer(resource);
	}

	public static void finish() {
		logger.info("Stopping ReceiverRestServer...");
		server.shutdownNow();
	}

}
