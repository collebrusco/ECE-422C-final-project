package gui.screens;

import gui.Client;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import network.controllers.LoginController;

public class LoginScreen extends Screen {

	public LoginScreen(Scene home) {
		super(home);		
	}

	@Override
	public Screen refresh() {
		return this;
	}

	@Override
	public void engage() {
		VBox vbox = new VBox();
		BorderPane border = genBorderPane(vbox, "Welcome to the Library 5000!");
		home.setRoot(border);
		ObservableList<Node> vlist = vbox.getChildren();
		vbox.setPadding(new Insets(10, 200, 10, 200));
		vbox.setSpacing(10);
		
		Text loginText = cText("Enter Username and Password:");
		Text utext = new Text("Username:");
		
		TextField uname = new TextField();
		
		Text ptext = cText("Password:");
		PasswordField pword = new PasswordField();
		
		vlist.addAll(loginText, utext, uname, ptext, pword);
		
		HBox butts = new HBox();
		
		Button cracct = new Button("Create Account...");
		Button login = new Button("Login");

		Region buttSpacer = new Region();

		HBox.setHgrow(buttSpacer, Priority.ALWAYS);
		
		butts.setSpacing(10);
		
		butts.getChildren().addAll(cracct, buttSpacer, login);
		vlist.add(butts);
		
		login.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				new LoginController(home).tryLogin(uname.getText(), pword.getText());
			}
		});
		
		border.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					new LoginController(home).tryLogin(uname.getText(), pword.getText());	
				}
			}
		});
		
		cracct.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Client.setScreen(new CreateAccountScreen(home));
			}
		});
	}

}
