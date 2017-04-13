package app.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import app.controller.ClientController;
import app.utility.HTTPHelper;


public class Server implements Runnable {
	
	private final static Logger LOGGER = Logger.getLogger(Runnable.class.getName());

	protected int port;

	private ServerSocket server;
	
	public static Map<Thread,Long> threadPool;
	
	static {
		threadPool = new ConcurrentHashMap<>();
	}

	@Override
	public void run() {
		try {
			server = new ServerSocket(port);
			System.err.println("API server started on port : " + server.getLocalPort());
			System.err.println("To stop it press <CTRL> + <C>");
			while (true) {
				Socket client = server.accept();
				String clientAddress = client.getRemoteSocketAddress().toString();
				System.err.println(clientAddress + " connected at "+ HTTPHelper.createHTTPDateString());
				ClientController communicator = new ClientController(client);
				Thread communication = new Thread(communicator, clientAddress);
				/*synchronized (threadPool) {
					threadPool.put(communication, 0L);
				}*/
				threadPool.put(communication, 0L);
				communication.start();
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public Server(int port) {
		this.port = port;
	}

	public Server() {
		this.port = 80;
	}
	

}
