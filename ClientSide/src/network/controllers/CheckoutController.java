package network.controllers;

import gui.Client;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import network.messages.CheckoutMessage;
import network.messages.Message;

public class CheckoutController extends Controller {
	
	String name;

	public void attemptCheckout(int id, String itemname){
		this.name = itemname;
		CheckoutMessage m = new CheckoutMessage();
		m.setIid(id);
		m.setUser(Client.sessionData.getAccount().getUsername());
		dispatch(m);
	}
	
	@Override
	public void execute(Message msg) {
		CheckoutMessage m = (CheckoutMessage)msg;
		if (m.isSuccess()) {
			Platform.runLater(()->{
				Alert a = new Alert(AlertType.INFORMATION);
				a.setHeaderText("Success!");
				a.setContentText("You have successfully checked out " + name + "!");
				a.show();
				Client.refresh();
			});
			return;
		} else {
			Platform.runLater(()->{
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Checkout Unsuccessful");
				a.setContentText(m.getResponse());
				a.show();
				Client.refresh();
			});
		}
	}

}
