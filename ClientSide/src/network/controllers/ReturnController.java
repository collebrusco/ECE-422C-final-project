package network.controllers;

import gui.Client;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import network.messages.Message;
import network.messages.ReturnMessage;

public class ReturnController extends Controller {
	
	public ReturnController() {
//		id = -1;
//		name = null;
	}
	
	public void tryReturn(int i, String n) {
//		id = i;
//		name = n;
		Message m = new ReturnMessage(i, n);
		dispatch(m);
	}
	
	
	@Override
	public void execute(Message msg) {
		ReturnMessage m = (ReturnMessage)(msg);
		if (!m.isValid()) {
			Platform.runLater(()->{
				Alert a = new Alert(AlertType.ERROR);
				a.setContentText("Item return failed, refresh and try again.");
				a.show();
			});
		} else {
			Platform.runLater(()->{
				Alert a = new Alert(AlertType.INFORMATION);
				a.setHeaderText("Success!");
				a.setContentText("Item returned.");
				a.show();
				Client.refresh();
			});
		}
	}
	
}
