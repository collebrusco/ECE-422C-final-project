package network.controllers;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

import gui.Client;
import network.Dispatcher;
import network.ServerHandler;
import network.messages.Message;

public abstract class Controller implements IController {

	private Dispatcher dispatcher;
	
	public Controller(){
		dispatcher = new Dispatcher(this);
	}
	
	public abstract void execute(Message msg);
	
	protected void dispatch(Message msg) {
		dispatcher.dispatch(msg);
	}

}
