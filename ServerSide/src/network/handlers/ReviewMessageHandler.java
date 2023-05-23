package network.handlers;

import data.Database;
import data.datatype.Item;
import network.messages.Message;
import network.messages.ReviewMessage;

public class ReviewMessageHandler extends MessageHandler {
	private ReviewMessage msg;
	protected ReviewMessageHandler(Message m) {
		super(m);
		msg = (ReviewMessage)m;
	}

	@Override
	public void handle(String sockID) {
		synchronized(Database.itemLock) {
			Item i = Database.readItem("_id", msg.getItemID());
			if (i.getPreviousBorrowers().contains(msg.getAuthor()) || i.getBorrower().equals(msg.getAuthor())) {
				msg.setSuccess(true);
				i.writeReview(msg.getAuthor(), msg.getReview());
				Database.updateItem(i);
			} else {
				msg.setSuccess(false);
			}
		}
	}

}
