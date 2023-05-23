package network.controllers;

import java.util.List;
import java.util.Observable;
import java.util.concurrent.ArrayBlockingQueue;

import data.datatype.Item;
import gui.Client;
import network.messages.ItemListMessage;
import network.messages.Message;

public class ItemListController extends Controller {
	
	List<Item> ilist;
	ItemListMessage msg;
	ArrayBlockingQueue<List<Item>> q;
	
	public ItemListController() {
		ilist = null;
		msg = null;
		q = new ArrayBlockingQueue<List<Item>>(1);
	}

	public List<Item> getItemList(){
		msg = new ItemListMessage();
		dispatch(msg);
		try {
			ilist = q.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return ilist;
	}

	@Override
	public void execute(Message msg) {
		try {
			q.put(((ItemListMessage)msg).getItemList());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
