package network.messages;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	public Message() {
		id = UUID.randomUUID().toString();
	}
	
	public String getID() {
		return id;
	}
	
	public boolean isResponse(Message m) {
		return this.id.toString().equals(m.getID());
	}
	
}
