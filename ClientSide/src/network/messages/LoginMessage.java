package network.messages;

import java.io.Serializable;

import data.datatype.Account;

public class LoginMessage extends Message {

	private static final long serialVersionUID = -4843267321541730023L;
	
	private String usr;
	private String pass;
	
	private LoginMessageResponse response;
	
	public LoginMessage(String u, String p){
		usr = u; pass = p; response = null;
	}
	
	public String getUsername() {
		return usr;
	}
	
	public String getPassword() {
		return pass;
	}
	
	public void setResponse(LoginMessageResponse r) {
		response = r;
	}
	
	public LoginMessageResponse getResponse() {
		return response;
	}
	
	public class LoginMessageResponse implements Serializable {
		private static final long serialVersionUID = -5255346394413124035L;
		private boolean valid;
		private Account account;
		private String error;
		
		public LoginMessageResponse() {
			account = null;
			error = null;
		}
		
		public boolean getValid() {
	        return valid;
	    }

	    public void setValid(boolean valid) {
	        this.valid = valid;
	    }
	    
	    public void setError(String err) {
	    	error = err;
	    }
	    
	    public String getError() {
	    	return error;
	    }

	    public Account getAccount() {
	        return account;
	    }

	    public void setAccount(Account account) {
	        this.account = account;
	    }
	}
	
}
