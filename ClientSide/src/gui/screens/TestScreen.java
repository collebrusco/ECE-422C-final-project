package gui.screens;

import java.net.URL;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class TestScreen extends Screen {
	private TextField field;
	public TestScreen(Scene home) {
		super(home);
	}
	@Override
	public Screen refresh() {
		return this;
	}
	@Override
	public void engage() {
		try {
			field = new TextField();
			GridPane grid = new GridPane();
			BorderPane border = genBorderPane(grid, "Test Screen!");
			home.setRoot(border);
			grid.add(field, 0, 0);
			
			URL url = new URL("https://amitabasu.files.wordpress.com/2021/01/the-stranger-book-cover.png");
			Image image = new Image(url.openStream());
			ImageView iview = new ImageView(image);
			
			grid.add(iview, 0, 1);
			
	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
