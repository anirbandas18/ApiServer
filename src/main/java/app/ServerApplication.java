package app;

import app.service.Server;

public class ServerApplication {

	public static void main(String args[]) throws Exception {
		System.setProperty("java.net.preferIPv4Stack", "true");
		int port = args.length == 0 ? 80 : Integer.parseInt(args[0]);
		Server server = new Server(port);
		String serverAddress = "localhost:" + port;
		Thread serverThread = new Thread(server, serverAddress);
		serverThread.start();
	}

}