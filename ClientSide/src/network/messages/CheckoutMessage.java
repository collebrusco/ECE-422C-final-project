package network.messages;

public class CheckoutMessage extends Message {

	private static final long serialVersionUID = -8412309817460879136L;

	private int iid;
	private String user;
	private String response;
	private boolean success;
	
	public CheckoutMessage() {
		user = null;
	}
	
	public void setIid(int iid) {
		this.iid = iid;
	}
	
	public int getIid() {
		return iid;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getUser() {
		return user;
	}
	
    public String getResponse() {
        return response;
    }
    
    public void setResponse(String response) {
        this.response = response;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }

}
