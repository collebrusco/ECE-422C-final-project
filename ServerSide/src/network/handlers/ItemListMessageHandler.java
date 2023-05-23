package network.handlers;

import java.util.ArrayList;

import data.Database;
import data.datatype.Item;
import network.messages.ItemListMessage;
import network.messages.Message;

public class ItemListMessageHandler extends MessageHandler {
	
	ItemListMessage msg;

	protected ItemListMessageHandler(Message m) {
		super(m);
		msg = (ItemListMessage)m;
	}

	@Override
	public void handle(String sockID) {
		ArrayList<Item> list = Database.readAllItems();
		msg.setItemList(list);
	}

}
