package network;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

import data.Database;
import network.handlers.MessageHandler;
import network.messages.Message;

public class ClientHandler implements Runnable {
	private Server server;
	private Socket client;
	
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	
	private String socketID;
	
	ClientHandler(Server s, Socket c){
		socketID = UUID.randomUUID().toString();
		server = s;
		client = c;
		try {
			objectOut = new ObjectOutputStream(client.getOutputStream());
			objectIn = new ObjectInputStream(client.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (server.running) {
			try {
				Message m = (Message)objectIn.readObject();
				MessageHandler.getHandler(m).handle(socketID);
				objectOut.writeObject(m);
			} catch (EOFException e) {
				System.out.println("connection to " + client + " closed");
				Database.logout(socketID);
				break;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
