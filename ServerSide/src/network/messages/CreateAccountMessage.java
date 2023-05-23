package network.messages;

public class CreateAccountMessage extends Message {

	private static final long serialVersionUID = -6704952230859138356L;

	private String username;
	private String password;
	
	private boolean created;
	
	public CreateAccountMessage() {
	}
	
	public CreateAccountMessage(String u, String p) {
		username = u;
		password = p;
	}
	
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isCreated() {
        return created;
    }
    
    public void setCreated(boolean created) {
        this.created = created;
    }
	
}
