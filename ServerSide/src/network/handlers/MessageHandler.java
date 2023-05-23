package network.handlers;

import network.messages.CheckoutMessage;
import network.messages.CreateAccountMessage;
import network.messages.ItemListMessage;
import network.messages.LoginMessage;
import network.messages.Message;
import network.messages.ModMessage;
import network.messages.ReturnMessage;
import network.messages.ReviewMessage;

public abstract class MessageHandler {
	
	public Message message;
	
	public abstract void handle(String sockID);
	
	protected MessageHandler(Message m) {
		message = m;
	}
	
	public static MessageHandler getHandler(Message msg) {
		if (msg instanceof LoginMessage) {
			return new LoginMessageHandler(msg);
		} else if (msg instanceof ItemListMessage) {
			return new ItemListMessageHandler(msg);
		} else if (msg instanceof CheckoutMessage) {
			return new CheckoutMessageHandler(msg);
		} else if (msg instanceof ReturnMessage) {
			return new ReturnMessageHandler(msg);
		} else if (msg instanceof CreateAccountMessage) {
			return new CreateAccountMessageHandler(msg);
		} else if (msg instanceof ReviewMessage) {
			return new ReviewMessageHandler(msg);
		} else if (msg instanceof ModMessage) {
			return new ModMessageHandler(msg);
		}
		
		return null;
	}
	
}
