package gui.screens;

import gui.Client;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import network.ServerHandler;

public class IPScreen extends Screen {

	public IPScreen(Scene home) {
		super(home);
	}

	@Override
	public Screen refresh() {
		return this;
	}

	@Override
	public void engage() {
		VBox box = new VBox();
		box.setPadding(new Insets(10, 200, 10, 200));
		box.setSpacing(10);
		Text t = cText("Server not found on localhost! Enter IP:");
		TextField ip = new TextField();
		Button b = new Button("try ip...");
		
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String ipstr = ip.getText();
				Client.serverHandler = new ServerHandler(ipstr);
				if (Client.serverHandler.isSuccess()) {
					Client.restart(home);
					Client.setScreen(new LoginScreen(home));
				}
			}
		});
		
		box.getChildren().addAll(t, ip, b);
		home.setRoot(box);
	}
	
	

}
