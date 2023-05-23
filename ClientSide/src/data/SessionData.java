package data;

import data.datatype.Account;

public class SessionData {

	private Account account;
	
	public SessionData() {
		account = null;
	}
	
	public void setAccount(Account a) {
		account = a;
	}
	
	public Account getAccount() {
		return account;
	}
}
