package network.controllers;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import network.messages.CreateAccountMessage;
import network.messages.Message;

public class CreateAccountController extends Controller {
	
	Scene home;
	
	public CreateAccountController(Scene home) {
		this.home = home;
	}

	public void tryCreate(String username, String password, String cpassword) {
		if (!password.equals(cpassword)) {
			Alert a = new Alert(AlertType.ERROR);
			a.setContentText("Passwords do not match!");
			a.show();
			return;
		}
		CreateAccountMessage m = new CreateAccountMessage(username, password);
		dispatch(m);
	}

	@Override
	public void execute(Message msg) {
		CreateAccountMessage m = (CreateAccountMessage)msg;
		if (m.isCreated()){
			Platform.runLater(()->{
				Alert a = new Alert(AlertType.INFORMATION);
				a.setHeaderText("Welcome " + m.getUsername() + "!");
				a.setContentText("Account created, logging in...");
				a.show();
				new LoginController(home).tryLogin(m.getUsername(), m.getPassword());
			});
		} else {
			Platform.runLater(()->{
				Alert a = new Alert(AlertType.ERROR);
				a.setContentText("Username taken. Try again...");
				a.show();
			});
		}
	}
}
