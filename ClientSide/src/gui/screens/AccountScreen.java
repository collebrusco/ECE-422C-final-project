package gui.screens;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Set;

import data.datatype.Account;
import data.datatype.Item;
import data.datatype.Item.ItemType;
import gui.Client;
import gui.Shapes;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import network.controllers.CheckoutController;
import network.controllers.Controller;
import network.controllers.ItemListController;
import network.controllers.ModController;
import network.controllers.ReturnController;

public class AccountScreen extends Screen {

	private BorderPane border;
	private VBox centerStack;
	private Node center;
	private boolean availFilter;
	private Set<ItemType> typefilters;
	private String searchTerm;
	private Node ssBox;
	protected boolean sidebarOpen;
	
	public AccountScreen(Scene home) {
		super(home);
		searchTerm = "";
		typefilters = new HashSet<ItemType>();
	}
	
	protected static VBox genVStack(Node[] stack) {
		VBox v = new VBox();
		v.setPadding(new Insets(10));
		v.setSpacing(10);
		v.getChildren().addAll(stack);
		return v;
	}
	
	protected static ImageView genItemImg(Item itm) {
		ImageView iview = null;
		InputStream is = new ByteArrayInputStream(itm.getImage());
		Image img = new Image(is);
		iview = new ImageView(img);
		
		iview.setPreserveRatio(true);
		iview.setFitHeight(200);
		return iview;
	}
	
	protected static HBox getDescBox(String description) {
		
		StringBuilder sb = new StringBuilder();
		boolean flag = false;
		for (int i = 0; i < description.length(); i++) {
			sb.append(description.charAt(i));
			if ((i+1) % 42 == 0) {
				flag = true;
			}
			if (flag && description.charAt(i) == ' ') {
				sb.append("\n");
				flag = false;
			}
		}
		description = sb.toString();
		Text desc = new Text(description);

		HBox box = new HBox(desc);
		box.getStyleClass().clear();
		box.getStyleClass().add("hbox-title");
		box.setPadding(new Insets(10));
		
		HBox.setHgrow(box, Priority.ALWAYS);

		return box;
	}

	protected static TitledPane getTDescBox(String title, String txt) {
		TitledPane dd = new TitledPane(title, getDescBox(txt));
		dd.setExpanded(false);
		return dd;
	}
	
	protected static Button genCheckoutButton(Item itm) {
		Button rentOrReturn = null;
		
		Polygon availShape = new Polygon();
		if (!itm.isAvailable() && Client.sessionData.getAccount().getUsername().equals(itm.getBorrower())) {
			availShape = Shapes.getUpArrow(80, 80, Color.GOLD);
		} else if (itm.isAvailable()) {
			availShape = Shapes.getCheck(1.5, Color.LIGHTGREEN);
		} else {
			availShape = Shapes.getOctogon(1.0, Color.SALMON);
		}
		
		// if you have item out, return button, else, rent button
		if (Client.sessionData.getAccount().getUsername().equals(itm.getBorrower())) {
			rentOrReturn = new Button("Return", availShape);
			rentOrReturn.setAlignment(Pos.CENTER);
			
			rentOrReturn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					new ReturnController().tryReturn(itm.getId(), Client.sessionData.getAccount().getUsername());
				}
			});
		} else {
			rentOrReturn = new Button("Checkout", availShape);
			rentOrReturn.setAlignment(Pos.CENTER);
			
			rentOrReturn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					new CheckoutController().attemptCheckout(itm.getId(), itm.getName());
				}
			});
		}
		rentOrReturn.setPrefSize(166, 45);
		return rentOrReturn;
	}
	
	protected Node genTypeDescStack(Item i) {
		Text type = cText(i.getType().getName());
		type.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, 18));
		
		TitledPane descDropDown = new TitledPane("Description...", getDescBox(i.getDescription()));
		descDropDown.setExpanded(false);
		return genVStack(new Node[] {getVSpacer(), type, descDropDown });
	}
	
	private BorderPane genItemListing(Item itm, int num) {
		HBox box = new HBox();
		BorderPane res = new BorderPane();

		Text name = cText(itm.getName());
		Text numTxt = cText("(" + Integer.toString(num) + ")");
		name.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, 24));
		numTxt.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, 24));
		HBox nameBox = new HBox(name);
		nameBox.getChildren().addAll(getHSpacer(), numTxt);
		nameBox.getStyleClass().clear();
		nameBox.getStyleClass().add("hbox-title");
		nameBox.setPadding(new Insets(10));
		res.setTop(nameBox);
		res.setCenter(box);
		box.setAlignment(Pos.CENTER);
		box.setSpacing(15);
		ImageView iv = genItemImg(itm);
		Screen thisScreen = this;
		iv.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Client.setScreen(new ItemScreen(thisScreen, home, itm));
			}
		});
		box.getChildren().add(iv);
		
		box.getChildren().add(genTypeDescStack(itm));
		
		Region spacer = getHSpacer();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		box.getChildren().add(spacer);
		
		Button rentOrReturn = null;
		
		
		rentOrReturn = genCheckoutButton(itm);
		box.getChildren().add(genVStack(new Node[] {getVSpacer(), rentOrReturn}));
		return res;
	}
	
	protected void engageBorder(Button b, String title) {
		border = new BorderPane();
		home.setRoot(border);
		
		HBox topBox = new HBox();
		
		Region topCenterSpacer = new Region();
		HBox.setHgrow(topCenterSpacer, Priority.ALWAYS);
		
		topBox.setPadding(new Insets(10));
		topBox.setSpacing(10);

		Text headerText = new Text(title == null ? "Library 5000" : title);
		headerText.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, 30));
		StackPane headerTextSP = new StackPane(headerText);
		headerTextSP.setAlignment(Pos.CENTER_LEFT);
        Polygon triangle = new Polygon(0, 0, 10, 0, 5, 10);

        ArrayList<Line> lines = new ArrayList<Line>();
        final int spread = 7;
        final int len = 24;
        lines.add(new Line(-len/2, 30-spread, len/2, 30-spread));
        lines.add(new Line(-len/2, 30, 		  len/2, 30));
        lines.add(new Line(-len/2, 30+spread, len/2, 30+spread));
        
        for (Line l : lines) {
        	l.setStroke(textColor);
        	l.setStrokeWidth(4);
        }
        
        Group root = new Group(lines.get(0), lines.get(1), lines.get(2));
        
		Button acct = new Button(Client.sessionData.getAccount().getUsername() + "'s Account", root);
		acct.setAlignment(Pos.CENTER);
		acct.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!sidebarOpen) {
					openSidebar();					
				} else {
					closeSidebar();
				}
			}
		});
		
		Button refresh = new Button("Refresh");
		refresh.setFont(new Font(12));
		refresh.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Client.refresh();
			}
		});
		
		if (b != null) {
			topBox.getChildren().add(b);
		}
		
		topBox.getChildren().addAll(headerTextSP, topCenterSpacer, refresh, acct);
		border.setTop(topBox);
		border.setBottom(getFooter());
	}
	
	public void openSidebar() {
		if (!sidebarOpen) {
			Node center = this.getCenter();
			HBox split = new HBox(center);
			HBox.setHgrow(center, Priority.ALWAYS);
			split.setPadding(new Insets(10));
			split.setSpacing(2);
			split.getStyleClass().clear();
			
			VBox bar = new VBox();
			ScrollPane sbar = new ScrollPane(bar);
			bar.setPadding(new Insets(2));
			bar.setSpacing(4);
			HBox.setHgrow(bar, Priority.NEVER);
			final int w = 250;
			bar.setMaxWidth(w-32);
			bar.setPrefWidth(w-32);
			bar.setMinWidth(w-32);
			bar.setAlignment(Pos.CENTER);
			sbar.setMaxWidth(w-24);
			sbar.setPrefWidth(w-24);
			sbar.setMinWidth(w-24);
			sbar.getStyleClass().clear();
			sbar.getStyleClass().add("scroll-trans");
			
			
			TitledPane items = genSBItems(w);
			if (Client.sessionData.getAccount().getAdmin()) {
				bar.getChildren().add(genAdminModControl(w));
			}
			bar.getChildren().addAll(items, genSBQuit(w));//genChat(w), items, genSBQuit(w));
			
			split.getChildren().add(sbar);
			this.setCenter(split);
			sidebarOpen = true;
		}
	}
	
//	private TitledPane genChat(int w) {
//		VBox itemstack = new VBox();
//		itemstack.setPadding(new Insets(2));
//		itemstack.setSpacing(2);
//		itemstack.setPrefWidth(w-32);
//		
//		TitledPane items = new TitledPane("Admin", itemstack);
//		items.setExpanded(false);
//		items.setPrefWidth(w-32);
//		//TODO
//		
//		return items;
//	}
	
	private TitledPane genAdminModControl(int w) {
		VBox itemstack = new VBox();
		itemstack.setPadding(new Insets(2));
		itemstack.setSpacing(2);
		itemstack.setPrefWidth(w-32);
		
		TitledPane items = new TitledPane("Admin", itemstack);
		items.setExpanded(false);
		items.setPrefWidth(w-32);
		
		Button b = new Button("Mod User");
		b.getStyleClass().clear();
		b.getStyleClass().add("button-title");
		b.setFont(Font.font("Helvetica", FontWeight.NORMAL, 18));
		b.setTextFill(Color.GOLD);
		b.setAlignment(Pos.CENTER);
		b.setPrefWidth(w-32);
		TextField f = new TextField();
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				new ModController().mod(f.getText());
			}
		});		
		
//		Region space = new Region();
//		space.setPrefHeight(20);
		itemstack.getChildren().addAll(f, b);//, space);
		
		return items;
	}
	
	private TitledPane genSBQuit(int w) {
		VBox itemstack = new VBox();
		itemstack.setPadding(new Insets(2));
		itemstack.setSpacing(2);
		itemstack.setPrefWidth(w-32);
		
		TitledPane items = new TitledPane("Exit...", itemstack);
		items.setExpanded(false);
		items.setPrefWidth(w-32);
		{
			Button b = new Button("Logout...");
			b.getStyleClass().clear();
			b.getStyleClass().add("button-title");
			b.setFont(Font.font("Helvetica", FontWeight.NORMAL, 18));
			b.setTextFill(Color.GOLD);
			b.setAlignment(Pos.CENTER);
			b.setPrefWidth(w-32);
			b.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					Client.logout();
				}
			});
			itemstack.getChildren().add(b);
		}{
			Button b = new Button("Quit...");
			b.getStyleClass().clear();
			b.getStyleClass().add("button-title");
			b.setFont(Font.font("Helvetica", FontWeight.NORMAL, 18));
			b.setTextFill(Color.RED);
			b.setAlignment(Pos.CENTER);
			b.setPrefWidth(w-32);
			b.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					Client.close();
				}
			});
			itemstack.getChildren().add(b);
		}
		
		return items;
	}
	
	private TitledPane genSBItems(int w) {
		VBox itemstack = new VBox();
		itemstack.setPadding(new Insets(2));
		itemstack.setSpacing(2);
		itemstack.setPrefWidth(w-32);
		
		TitledPane items = new TitledPane("My Items", itemstack);
		items.setExpanded(false);
		items.setPrefWidth(w-32);
		
		List<Item> ilist = new ItemListController().getItemList();
		Iterator<Item> it = ilist.iterator();
		while (it.hasNext()) {
			Item i = it.next();
			if (!i.getBorrower().equals(Client.sessionData.getAccount().getUsername())) {
				it.remove();
			}
		}
		
		if (ilist.size() == 0) {
			Text none = cText("No items");
			itemstack.getChildren().add(none);
		} else {
			for (Item i : ilist) {
				Button b = new Button(i.getName());
				b.getStyleClass().clear();
				b.getStyleClass().add("button-title");
				b.setFont(Font.font("Helvetica", FontWeight.NORMAL, 18));
				b.setAlignment(Pos.CENTER);
				ImageView iv = this.genItemImg(i);
				iv.setFitHeight(40);
				b.setGraphic(iv);
				b.setPrefWidth(w-32);
				Screen _this = this;
				b.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						closeSidebar();
						Client.setScreen(new ItemScreen(Client.screen, _this.home, i));
					}
				});
				itemstack.getChildren().add(b);
			}
		}
		return items;
	}

	public void closeSidebar() {
		if (!(this.getCenter() instanceof HBox)) {
			sidebarOpen = false;
		}
		if (sidebarOpen) {
			Node center = ((HBox)this.getCenter()).getChildren().get(0);
			this.setCenter(center);
			sidebarOpen = false;
		}
	}
	
	public void engage() {
		engageBorder(null, null);
		List<Item> ilist = new ItemListController().getItemList();
		
		centerStack = new VBox();
		ScrollPane scroll = new ScrollPane(centerStack);
		scroll.setFitToWidth(true);
		centerStack.setAlignment(Pos.CENTER);
		
		centerStack.setPadding(new Insets(10, 50, 10, 50));
		centerStack.setSpacing(10);
		
		ssBox = genSearchSortBox();
		
		refreshCenterStack(ilist);
		
		this.setCenter(scroll);
	}
	
	private void refreshCenterStack(List<Item> ilist) {
		centerStack.getChildren().clear();
		centerStack.getChildren().add(ssBox);
		
		int i = 0;
		for (Item itm : ilist) {
			centerStack.getChildren().add(genItemListing(itm, ++i));
		}
	}

	private Node genSearchSortBox() {
		HBox box = new HBox();
		
		box.setPadding(new Insets(10));
		box.setSpacing(10);
		box.getStyleClass().add("hbox-search");
		
		Text search = cText("Search:");
		StackPane searchsp = new StackPane(search);
		searchsp.setAlignment(Pos.CENTER);
		search.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
		TextField searchField = new TextField();
		searchField.setAlignment(Pos.CENTER);
//		searchField.setPrefWidth(400);
		Button searchButt = new Button("Go");

		CheckBox avail = new CheckBox("Available");
		CheckBox book = new CheckBox("Book");
		CheckBox dvd = new CheckBox("DVD");
		CheckBox game = new CheckBox("Game");
		
		searchButt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				searchTerm = searchField.getText();
				availFilter = avail.isSelected();
				if (book.isSelected()) { typefilters.add(ItemType.BOOK); } else { typefilters.remove(ItemType.BOOK); }
				if (dvd.isSelected()) { typefilters.add(ItemType.DVD); } else { typefilters.remove(ItemType.DVD); }
				if (game.isSelected()) { typefilters.add(ItemType.GAME); } else { typefilters.remove(ItemType.GAME); }
				Client.screen.refresh();
			}
		});
		
		Text filters = cText("Filters:");
		filters.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
		StackPane filtersp = new StackPane(filters);
		filtersp.setAlignment(Pos.CENTER);
		
		Button applyButt = new Button("Apply");
		applyButt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent a) {
				searchTerm = searchField.getText();
				availFilter = avail.isSelected();
				if (book.isSelected()) { typefilters.add(ItemType.BOOK); } else { typefilters.remove(ItemType.BOOK); }
				if (dvd.isSelected()) { typefilters.add(ItemType.DVD); } else { typefilters.remove(ItemType.DVD); }
				if (game.isSelected()) { typefilters.add(ItemType.GAME); } else { typefilters.remove(ItemType.GAME); }
				Client.screen.refresh();
			}
		});
		
		box.setAlignment(Pos.CENTER);
		box.getChildren().addAll(searchsp, searchField, searchButt, getHSpacer(), filtersp, avail, book, dvd, game, applyButt);
		
		return box;
	}

	protected void setCenter(Node center) {
		this.center = center;
		border.setCenter(center);
	}
	
	protected Node getCenter() {
		return this.center;
	}

	@Override
	public Screen refresh() {
		System.out.println("refreshing account screen");
		List<Item> ilist = new ItemListController().getItemList();
		filterItemList(ilist);
		refreshCenterStack(ilist);
		if (sidebarOpen) {
			closeSidebar();
			openSidebar();
		}
		return this;
	}

	private void filterItemList(List<Item> ilist) {
		System.out.println("filtering ilist");
		Iterator<Item> it = ilist.iterator();
		while(it.hasNext()) {
			Item i = it.next();
			if ((!searchTerm.equals("")) && (!i.getName().toLowerCase().contains(searchTerm.toLowerCase()))) {
				it.remove();
				continue;
			}
			if (availFilter && (!i.isAvailable())) {
				it.remove();
				continue;
			}
			if ((!typefilters.isEmpty()) && (!typefilters.contains(i.getType()))) {
				it.remove();
				continue;
			}
		}
	}
	
}
