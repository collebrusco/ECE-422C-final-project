package gui.screens;

import java.util.Iterator;
import java.util.List;

import data.datatype.Item;
import gui.Client;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import network.controllers.ItemListController;
import network.controllers.ReviewController;

public class ItemScreen extends AccountScreen {
	
	private Parent oldRoot;
	private Screen oldScreen;
	private Item item;
	private ScrollPane scroll;
	
	public ItemScreen(Screen former, Scene home, Item i) {
		super(home);
		oldScreen = former;
		item = i;
	}
	
	@Override
	public void engage() {
		oldRoot = home.getRoot();
		Button backButt = new Button("Back");
		backButt.setFont(new Font(12));
		backButt.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				home.setRoot(oldRoot);
				Client.setScreen(oldScreen);
			}
		});
		this.engageBorder(backButt, item.getName());
		
		scroll = new ScrollPane(genItemPane());
		scroll.setFitToHeight(true);
		scroll.setFitToWidth(true);
		this.setCenter(scroll);
		
	}
	
	private Node genItemPane() {
		AnchorPane ap = new AnchorPane();
		
		
		ImageView iv = genItemImg(item);
		iv.setPreserveRatio(true);
		iv.setFitHeight(400);
		VBox left = new VBox();
		left.setPadding(new Insets(10));
		left.setSpacing(10);
		Text type = cText(item.getType().getName());
		type.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, 18));
		

		Text lastdate = cText(item.getDate().equals("") ? "" : "Last Checked Out: " + item.getDate());
		lastdate.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, 18));
		
		Text dtext = cText(item.getDescription());
		dtext.setFont(Font.font("Helvetica", FontWeight.NORMAL, 14));
		final int rw = 440;
		dtext.setWrappingWidth(rw);
		VBox dtextbox = new VBox(dtext);
		dtextbox.setPadding(new Insets(6));
		
		TitledPane descDropDown = new TitledPane("Description...", dtextbox);//getDescBox(item.getDescription()));
		descDropDown.setExpanded(false);
		Button cbutt = genCheckoutButton(item);
		left.getChildren().addAll(iv, getVSpacer(), type, lastdate, cbutt);
		AnchorPane.setLeftAnchor(left, 10.0);
		ap.getChildren().add(left);
		
		VBox right = new VBox();
		right.setPadding(new Insets(10));
		right.setSpacing(10);
		right.setPrefWidth(rw);
		
		TitledPane previous = new TitledPane("Previous Borrowers", genPreviousBorrowerStack(item));
		previous.setExpanded(false);
		
		TitledPane reviews = new TitledPane("Reviews", genReviewStack(item));
		reviews.setExpanded(false);
		
		TitledPane wlist = new TitledPane("Waitlist", genWaitlist(item));
		wlist.setExpanded(false);
		
		descDropDown.setPrefWidth(rw);
		previous.setPrefWidth(rw);
		reviews.setPrefWidth(rw);
		
		right.getChildren().addAll(descDropDown, wlist, previous, reviews);
				
		ap.getChildren().add(right);
		AnchorPane.setBottomAnchor(right, 10.0);
		AnchorPane.setRightAnchor(right, 10.0);
		
		return ap;
	}
	
	private VBox genWaitlist(Item i) {
		VBox rs = new VBox();
		rs.setPadding(new Insets(6));
		rs.setSpacing(10);
		Iterator<String> it = item.getWaitlist().iterator();
		if (!item.isAvailable()) {
			Text b = cText("Currently checked out by: " + item.getBorrower());
			b.setFont(Font.font("Helvetica", FontWeight.BOLD, 14));
			rs.getChildren().add(b);
		}
		if (!it.hasNext()) {
			Text b = cText("Waitlist is empty");
			b.setFont(Font.font("Helvetica", FontWeight.BOLD, 14));
			rs.getChildren().add(b);
		}
		while (it.hasNext()) {
			Text b = cText(it.next());
			b.setFont(Font.font("Helvetica", FontWeight.BOLD, 14));
			rs.getChildren().add(b);
		}
		rs.setPrefWidth(440);
		return rs;
	}
	
	private VBox genPreviousBorrowerStack(Item i) {
		VBox rs = new VBox();
		rs.setPadding(new Insets(6));
		rs.setSpacing(10);
		for (int k = item.getPreviousBorrowers().size()-1; k >= 0; k--) {
			Text b = cText(item.getPreviousBorrowers().get(k));
			b.setFont(Font.font("Helvetica", FontWeight.BOLD, 14));
			rs.getChildren().add(b);
		}
		rs.setPrefWidth(440);
		return rs;
	}

	private VBox genReviewStack(Item i) {
		VBox rs = new VBox();
		rs.setPadding(new Insets(6));
		rs.setSpacing(10);
		Button leaveReview = new Button("Post review...");
		TextField reviewField = new TextField();
		reviewField.setPrefWidth(440);
		rs.getChildren().addAll(leaveReview, reviewField);
		
		leaveReview.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				new ReviewController(reviewField.getText(), i.getId()).leaveReview();
			}
		});
		
		for (int k = item.getReviews().size()-1; k >= 0; k--) {
			String[] parse = Item.parseReview(item.getReviews().get(k));
			VBox rb = new VBox();
			rb.setPadding(new Insets(2));
			rb.setSpacing(2);
			Text auth = cText(parse[0] + ":");
			auth.setTextAlignment(TextAlignment.CENTER);
			auth.setFont(Font.font("Helvetica", FontWeight.BOLD, 14.0));
			auth.getStyleClass().add("hbox-title");
			Text rev = cText(parse[1]);
			rev.setWrappingWidth(600);
			rb.getChildren().addAll(auth, rev);
			rs.getChildren().add(rb);
		}
		rs.setPrefWidth(440);
		return rs;
	}

	@Override
	public Screen refresh() {
		System.out.println("refreshing item screen");
		List<Item> ilist = new ItemListController().getItemList();
		for (Item i : ilist) {
			if (i.equals(item)) {
				item = i;
				scroll.setContent(genItemPane());
				if (super.sidebarOpen) {
					closeSidebar();
					openSidebar();
				}
			}
		}
		return this;
	}

}
