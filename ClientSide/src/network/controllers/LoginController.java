package network.controllers;

import java.util.Observable;

import gui.Client;
import gui.screens.AccountScreen;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import network.messages.LoginMessage;
import network.messages.Message;

public class LoginController extends Controller {
	private Scene home;
	private Message msg;

	public LoginController(Scene s) {
		home = s;
	}
	
	public void tryLogin(String user, String pass) {
		msg = new LoginMessage(user, pass);
		dispatch(msg);
	}

	@Override
	public void execute(Message msg) {
		LoginMessage lm = (LoginMessage)(msg);
		if (lm.getResponse().getValid()) {
			Client.sessionData.setAccount(lm.getResponse().getAccount());
			Platform.runLater(() -> {
				Client.setScreen(new AccountScreen(home));
			});
		} else {
			Platform.runLater(()->{
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Login failed!");
				a.setContentText(lm.getResponse().getError());
				a.show();
			});
		}
	}
}
