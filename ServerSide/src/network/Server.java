package network;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.mongodb.MongoSocketReadException;
import com.mongodb.MongoSocketReadTimeoutException;

import data.Database;

public class Server {
	private ServerSocket serverSocket;
	public boolean running;
	private final String ip = "localhost";
	
	
	Server(){		
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println("WARNING: Lost connection to database, rebooting...");
				e.printStackTrace();
				Database.init();
			}
		});
		try {
			Database.init();
		} catch (MongoSocketReadException e) {
			System.out.println("ERROR: can't connect to mongoDB! Check if IP is whitelisted");
		}

	}
	
	private void cout(String msg) {
		System.out.println(msg);
	}
	
	public static void main(String[] args) {
		new Server().start(args);
	}

	private void start(String[] args) {
		try {
			startServer();
			clientConnectLoop();
			shutdown();
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MongoSocketReadTimeoutException e) {
			System.out.println("WARNING: Mongo Socket Timeout, rebooting...");
			Database.init();
		}
		shutdown();
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean startServer() {
		try {
			//TODO close this somewhere when Im done?
			serverSocket = new ServerSocket(8080, 50, InetAddress.getByName(ip));
//			serverSocket = new ServerSocket(8080, 50);
			running = true;
		} catch (IOException e) {
			System.out.println("ERROR: Failed to start server!");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void shutdown() {
		running = false;
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void clientConnectLoop() {
		while (running) {
			try {
				Socket clientSocket = serverSocket.accept();
				cout("connecting to client " + clientSocket);
				ClientHandler handler = new ClientHandler(this, clientSocket);
				
				new Thread(handler).start();
				
			} catch (IOException e) {
				System.out.println("Server is closed, ceasing accepts");
				break;
			}
		}
	}
	
}

