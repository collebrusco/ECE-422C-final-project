package network;

import java.util.Observable;
import java.util.Observer;

import gui.Client;
import network.controllers.IController;
import network.messages.Message;

public class Dispatcher implements Observer {

	ServerHandler serverHandler;
	IController controller;
	Message message;
	
	public Dispatcher(IController home){
		serverHandler = Client.serverHandler;
		controller = home;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		Message m = (Message)arg;
		if (message.isResponse(m)) {
			message = m;
			controller.execute(m);
			delist();
		}
	}

	public void dispatch(Message msg) {
		message = msg;
		enlist();
		serverHandler.dispatch(msg);
		
	}
	
	private void enlist() {
		serverHandler.addObserver(this);
	}
	
	private void delist() {
		serverHandler.deleteObserver(this);
	}
	
}
