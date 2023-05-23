package gui.screens;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public abstract class Screen {
	protected Scene home;
	
	protected static Color textColor = Color.web("#2b363d");
	
	Screen(Scene home){
		this.home = home;
	}
	
	public abstract Screen refresh();
	
	public abstract void engage();
	
	public Scene getHome() {
		return home;
	}
	
	protected static Text cText(String txt) {
		Text t = new Text(txt);
		t.setFill(textColor);
		return t;
	}
	
	public static Region getHSpacer() {
		Region buttSpacer = new Region();
		HBox.setHgrow(buttSpacer, Priority.ALWAYS);
		return buttSpacer;
	}	
	
	public static Region getVSpacer() {
		Region buttSpacer = new Region();
		VBox.setVgrow(buttSpacer, Priority.ALWAYS);
		return buttSpacer;
	}
	
	public static Region getGSpacer() {
		Region buttSpacer = new Region();
		GridPane.setHgrow(buttSpacer, Priority.ALWAYS);
		GridPane.setVgrow(buttSpacer, Priority.ALWAYS);
		return buttSpacer;
	}
	
	protected Parent getHeader(String header_text){
		Text headerText = new Text(header_text);
		headerText.setFont(Font.font("Helvetica",FontWeight.EXTRA_BOLD, 30));
		headerText.setFill(textColor);
		HBox header = new HBox();
		header.getChildren().add(headerText);
		Text projLabel = new Text("Final Project");
		projLabel.setTextAlignment(TextAlignment.RIGHT);
		projLabel.setFill(textColor);
		Region r = getHSpacer();
		header.getChildren().addAll(r, projLabel);
		header.setPadding(new Insets(10));
		header.setSpacing(20);
		return header;
	}
	
	protected Parent getFooter(){
		HBox footer = new HBox();
		Text t = new Text("Created by Frank Collebrusco");
		t.setFill(textColor);
		footer.getChildren().add(t);
		footer.setPadding(new Insets(5,10,5,10));
		footer.setSpacing(50);
		return footer;
	}
	
	protected BorderPane genBorderPane(Node center, String header) {
		BorderPane b = new BorderPane();
		b.setTop(getHeader(header));
		b.setBottom(getFooter());
		b.setCenter(center);
		return b;
	}
	
}
