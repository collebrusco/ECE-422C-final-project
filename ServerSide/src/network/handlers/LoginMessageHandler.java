package network.handlers;

import java.util.Optional;

import data.Database;
import data.datatype.Account;
import network.messages.LoginMessage;
import network.messages.LoginMessage.LoginMessageResponse;
import network.messages.Message;
import util.Tuple;

public class LoginMessageHandler extends MessageHandler {

	private LoginMessage msg;
	
	public LoginMessageHandler(Message msg) {
		super(msg);
		this.msg = (LoginMessage)msg;
	}
	
	@Override
	public void handle(String sockID) {
		LoginMessageResponse resp = msg.new LoginMessageResponse();
		Tuple<Account, Optional<String>> result = Database.validateAccount(msg.getUsername(), msg.getPassword(), sockID);
		final Account a = result.getFirst();
		resp.setValid(a != null);
		resp.setAccount(a);
		resp.setError(result.getSecond().orElse(""));
		msg.setResponse(resp);
	}

}
