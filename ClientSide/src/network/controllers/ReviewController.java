package network.controllers;

import gui.Client;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import network.messages.Message;
import network.messages.ReviewMessage;

public class ReviewController extends Controller {

	private String review;
	private int itemID;
	
	public ReviewController(String review, int itemID){
		this.review = review;
		this.itemID = itemID;
	}
	
	public void leaveReview(){
		ReviewMessage m = new ReviewMessage(itemID, review, Client.sessionData.getAccount().getUsername());
		dispatch(m);
	}
	
	@Override
	public void execute(Message msg) {
		ReviewMessage m = (ReviewMessage)msg;
		if (m.isSuccess()) {
			Platform.runLater(()->{
				Alert a = new Alert(AlertType.INFORMATION);
				a.setHeaderText("Success");
				a.setContentText("Your review has been placed.");
				a.show();
				Client.screen.refresh();
			});
		} else {
			Platform.runLater(()->{
				Alert a = new Alert(AlertType.ERROR);
				a.setContentText("You've never checked out this item! Try it first");
				a.show();
			});
		}
	}
}
