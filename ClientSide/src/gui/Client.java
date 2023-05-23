package gui;
	
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import data.SessionData;
import gui.screens.IPScreen;
import gui.screens.LoginScreen;
import gui.screens.Screen;
import gui.screens.TestScreen;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import network.ServerHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/*
 * IDEAS
 * client set profile pic by entering URL?
 * 
 */

public class Client extends Application {
	
	private static Thread serverThread;
	public static ServerHandler serverHandler;
	
	public static SessionData sessionData;
	public static Screen screen;	
	
	public static void setScreen(Screen s) {
		screen = s;
		screen.engage();
	}
	
	public static void refresh() {
		screen = screen.refresh();
		screen.refresh();
	}
	
	public static void close() {
		if (serverThread != null) {
			serverThread.stop();
		}
		Client.logout();
		System.out.println("closing client...");
		if (serverHandler != null) {
			serverHandler.close();
		}
		if (serverThread != null) {
			serverThread.stop();
		}
		Platform.exit();
	}
	
	public static void logout() {
		serverHandler.close();
		serverHandler = new ServerHandler();
		if (!serverHandler.isSuccess()) {
//			Alert a = new Alert(AlertType.ERROR);
//			a.setContentText("Could not reach server! Exiting...");
//			a.showAndWait();
//			Platform.exit();
			Client.setScreen(new IPScreen(Client.screen.getHome()));
			return;
		}
		serverThread = new Thread(serverHandler);
		serverThread.start();
		Client.setScreen(new LoginScreen(Client.screen.getHome()));
	}
	
	public static void restart(Scene scene) {
		serverThread = new Thread(serverHandler);
		serverThread.start();
		screen = new LoginScreen(scene);
	}
	
	@Override
	public void start(Stage primaryStage) {
		Scene scene = new Scene(new GridPane(),1200,800);
		sessionData = new SessionData();
		serverHandler = new ServerHandler();
		if (!serverHandler.isSuccess()) {
			Client.setScreen(new IPScreen(scene));
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			screen.engage();
			primaryStage.setOnCloseRequest(event -> {
				Client.close();
			});
			primaryStage.setMinWidth(800);
			primaryStage.setScene(scene);
			primaryStage.show();
//			Alert a = new Alert(AlertType.ERROR);
//			a.setContentText("Could not reach server! Exiting...");
//			a.showAndWait();
//			Platform.exit();
			return;
		}
		serverThread = new Thread(serverHandler);
		serverThread.start();
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		screen = new LoginScreen(scene);
		screen.engage();
		
			
		primaryStage.setOnCloseRequest(event -> {
			Client.close();
		});
		primaryStage.setMinWidth(800);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	

}
