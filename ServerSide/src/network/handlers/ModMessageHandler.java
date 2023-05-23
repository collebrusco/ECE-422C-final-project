package network.handlers;

import data.Database;
import network.messages.Message;
import network.messages.ModMessage;

public class ModMessageHandler extends MessageHandler {

	ModMessage msg;
	
	public ModMessageHandler(Message m) {
		super(m);
		msg = (ModMessage)m;
	}

	@Override
	public void handle(String sockID) {
		String error = Database.mod(msg.getUser());
		msg.setSuccess(error == null);
		msg.setError(error);
	}

}
