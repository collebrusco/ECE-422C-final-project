package network.messages;

public class ModMessage extends Message {
	private static final long serialVersionUID = -2074915402284129110L;
	String user;
	boolean success;
	String error;
	
    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
}
