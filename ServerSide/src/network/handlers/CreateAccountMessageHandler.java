package network.handlers;

import data.Database;
import network.messages.CreateAccountMessage;
import network.messages.Message;

public class CreateAccountMessageHandler extends MessageHandler {

	private CreateAccountMessage msg;
	
	protected CreateAccountMessageHandler(Message m) {
		super(m);
		msg = (CreateAccountMessage)m;
	}

	@Override
	public void handle(String sockID) {
		msg.setCreated(Database.tryCreateAccount(msg.getUsername(), msg.getPassword()));
	}

}
