package network.handlers;

import java.util.Optional;

import data.Database;
import network.messages.CheckoutMessage;
import network.messages.Message;

public class CheckoutMessageHandler extends MessageHandler {

	protected CheckoutMessageHandler(Message m) {
		super(m);
	}

	@Override
	public void handle(String sockID) {
		CheckoutMessage m = (CheckoutMessage)this.message;
//		System.out.println("got co req, checking w db...");
		Optional<String> res = Database.attemptCheckout(sockID, m.getIid());
//		System.out.println("req is: " + res.isPresent());
		m.setSuccess(!res.isPresent());
		m.setResponse(res.orElse(null));
	}

}
