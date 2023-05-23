package network.messages;

import java.util.ArrayList;

import data.datatype.Item;

public class ItemListMessage extends Message {

	private static final long serialVersionUID = 593926324873120004L;
	
	private ArrayList<Item> list;

	public void setItemList(ArrayList<Item> list) {
		this.list = list;
	}
	
	public ArrayList<Item> getItemList(){
		return list;
	}
}
