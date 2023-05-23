package network.handlers;

import data.Database;
import network.messages.Message;
import network.messages.ReturnMessage;

public class ReturnMessageHandler extends MessageHandler {
	
	ReturnMessage msg;

	protected ReturnMessageHandler(Message m) {
		super(m);
		msg = (ReturnMessage)m;
	}

	@Override
	public void handle(String sockID) {
		msg.setValid(Database.tryReturnItem(msg.getItemId(), msg.getUsername()));
	}

}
