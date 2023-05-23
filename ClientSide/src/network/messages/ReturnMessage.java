package network.messages;

public class ReturnMessage extends Message {
	private static final long serialVersionUID = -6776532371053713295L;
	
	private int itemId;
	private String username;
	private boolean valid;
	
	public ReturnMessage() {
		super();
	}
	
	public ReturnMessage(int i, String u) {
		super();
		this.itemId = i;
		this.username = u;
	}
	
	public void setItemId(int id) {
		this.itemId = id;
	}
	
	public int getItemId() {
		return itemId;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	public boolean isValid() {
		return valid;
	}
	
}
