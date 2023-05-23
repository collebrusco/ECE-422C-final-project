package network.controllers;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import network.messages.Message;
import network.messages.ModMessage;

public class ModController extends Controller {
	
	ModMessage m;
	
	public void mod(String user) {
		m = new ModMessage();
		m.setUser(user);
		dispatch(m);
	}

	@Override
	public void execute(Message msg) {
		m = (ModMessage)msg;
		if (m.isSuccess()) {
			Platform.runLater(()->{
				Alert a = new Alert(AlertType.INFORMATION);
				a.setHeaderText("Success!");
				a.setContentText("User " + m.getUser() + " is now an admin");
				a.show();
			});
		} else {
			Platform.runLater(()->{
				Alert a = new Alert(AlertType.ERROR);
				a.setContentText(m.getError());
				a.show();
			});
		}
	}

}
