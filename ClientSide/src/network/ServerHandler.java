package network;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;
import java.util.Observer;

import network.messages.Message;

public class ServerHandler extends Observable implements Runnable {
	
	private final String ip = "localhost";
	private Socket server;
	private boolean running;
	private boolean success;
	
	private ObjectOutputStream objectOut;
	private ObjectInputStream objectIn;
	
	public ServerHandler(){
		try {
			server = new Socket(ip, 8080);
			System.out.println("connecting to server " + server);
			objectOut = new ObjectOutputStream(server.getOutputStream());
			objectIn = new ObjectInputStream(server.getInputStream());
		} catch (Exception e) {
			System.out.println("ERROR: failed to establish connection to server!");
			success = false;
			return;
		}
		success = true;
	}
	
	public ServerHandler(String ip){
		try {
			System.out.println("trying ip: " + ip);
			server = new Socket(ip, 8080);
			System.out.println("connecting to server " + server);
			objectOut = new ObjectOutputStream(server.getOutputStream());
			objectIn = new ObjectInputStream(server.getInputStream());
		} catch (Exception e) {
			System.out.println("ERROR: failed to establish connection to server!");
			success = false;
			return;
		}
		success = true;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public void close() {
		running = false;
		try {
			System.out.println("closing IO streams... ");
			if (objectIn != null) {
				objectIn.close();
			}
			if (objectOut != null) {
				objectOut.close();
			}
			System.out.println("done");
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}
	
	public synchronized void dispatch(Message m) {
		try {
			objectOut.writeObject(m);
		} catch (IOException e) {
			System.out.println("ERROR: failed to send message to server!");
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		running = true;
		if (!success) {return;}
		try {
			while (running) {
				try {
				Message m = (Message)objectIn.readObject();
				setChanged();
				notifyObservers(m);
				} catch (SocketException e) {
					System.out.println("Serverhandler lost socket, closing...");
					return;
				}
			}
		} catch (EOFException e) {
		    System.out.println("Socket closed.");
		    try {
				server.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		}
	}

}
