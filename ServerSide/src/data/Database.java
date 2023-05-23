package data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

import data.datatype.Account;
import data.datatype.Item;
import data.datatype.Item.ItemType;
import util.Tuple;

public class Database {
	
	private static final String mongoURL = "";
	
	private static MongoClient mongoClient;
	private static MongoDatabase db;

	private static CodecRegistry pojoCodecRegistry;
	private static CodecRegistry codecRegistry;
	private static CodecRegistry customCodecRegistry;
    
	private static MongoCollection<Account> accountCollection;
	private static MongoCollection<Item> itemCollection;
	public static Object acctLock;
	public static Object itemLock;
	public static Object initLock;
	
	// accounts to socket toStrings
	private static Map<String, String> activeAccounts = new HashMap<>();
	
	public static void logout(String socket) {
		Set<String> keyset = activeAccounts.keySet();
		String rem = null;
		{
			Iterator<String> it = keyset.iterator();
			while (it.hasNext()) {
				String n = it.next();
				if (activeAccounts.get(n).equals(socket)) {
					rem = n;
					break;
				}
			}
		}
		if (rem != null) {
			activeAccounts.remove(rem);
		}
	}
	
	private static String getUserFromSock(String sockID) {
		Set<String> keyset = activeAccounts.keySet();
		{
			Iterator<String> it = keyset.iterator();
			while (it.hasNext()) {
				String n = it.next();
				if (activeAccounts.get(n).equals(sockID)) {
					return n;
				}
			}
		}
		return null;
	}
	
	public static void init() {		
		acctLock = new Object();
		itemLock = new Object();
		initLock = new Object();
		synchronized(initLock){		
			mongoClient = MongoClients.create(mongoURL);
	
			db = mongoClient.getDatabase("library");
	
			pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
			codecRegistry = MongoClientSettings.getDefaultCodecRegistry();
			customCodecRegistry = CodecRegistries.fromRegistries(codecRegistry, pojoCodecRegistry);
		    
			accountCollection = db.getCollection("accounts", Account.class).withCodecRegistry(customCodecRegistry);
			itemCollection = db.getCollection("items", Item.class).withCodecRegistry(customCodecRegistry);
		}
	}
	
	public static boolean accountExists(String username) {
		synchronized(acctLock) {
			FindIterable<Account> query = accountCollection.find(Filters.eq("username", username));
			MongoCursor<Account> it = query.iterator();
			return it.hasNext();
		}
	}
	
	public static boolean tryCreateAccount(String username, String password) {
		synchronized(acctLock) {
			if (accountExists(username)) {
				return false;
			} else {
				Account acct = new Account();
				acct.setUsername(username);
				acct.setPassword(password);
				InsertOneResult a = accountCollection.insertOne(acct);
				return a.wasAcknowledged();
			}
		}
	}
	
	public static Tuple<Account, Optional<String>> validateAccount(String username, String password, String socket) {
		synchronized(acctLock) {
			Tuple<Account, Optional<String>> res = new Tuple<Account, Optional<String>>();
			res.setFirst(readAccount(username));
			if ((res.getFirst() != null) && (!(res.getFirst().getPassword().equals(password)))) {
				res.setSecond(Optional.of("Incorrect password!"));
				res.setFirst(null);
			} else if ((res.getFirst() != null) && activeAccounts.containsKey(username)) {
				System.out.println("Account " + username + " is already logged in");
				res.setSecond(Optional.of("Account " + username + " is already logged in"));
				res.setFirst(null);
			} else if ((res.getFirst() != null)) {
				System.out.println(username + " logged in");
				res.setSecond(Optional.empty());
				activeAccounts.put(res.getFirst().getUsername(), socket);
			} 
			else if (res.getFirst() == null) {
				System.out.println("Account \"" + username + "\" does not exist!");
				res.setSecond(Optional.of("Account \"" + username + "\" does not exist!"));
			}
			return res;
		}
	}
	
	private static String getDate() {
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm MM/dd/yyyy");
		return format.format(date);
	}
	
	public static Optional<String> attemptCheckout(String sockID, int id){
		synchronized(itemLock) {
			Item i = readItem("_id", id);
			if (i == null) {
				return Optional.of("Item not found");
			}
			if (i.isAvailable()) {
				i.setAvailable(false);
				i.setBorrower(getUserFromSock(sockID));
				i.setDate(getDate());
				updateItem(i);
				return Optional.empty();
			} else {
				if (!i.getWaitlist().contains(getUserFromSock(sockID))) {
					i.getWaitlist().add(getUserFromSock(sockID));
					updateItem(i);
				}
				return Optional.of("Item is already checked out, you're on the waitlist");
			}
		}
	}

//	public static void returnItem(int id) {
//		synchronized(itemLock) {
//			Item i = readItem("_id", id);
//			i.setAvailable(true);
//			i.getPreviousBorrowers().add(i.getBorrower());
//			i.setBorrower("");
//			updateItem(i);
//		}
//	}
	
	public static boolean tryReturnItem(int id, String username) {
		synchronized(itemLock) {
			Item i = readItem("_id", id);
			System.out.println(username + " is attempting to return " + i.getName());
			if (i.getBorrower().equals(username)) {
				if (i.getWaitlist().isEmpty()) {
					i.setAvailable(true);
					i.getPreviousBorrowers().add(i.getBorrower());
					i.setBorrower("");
					updateItem(i);
					return true;
				} else {
					i.getPreviousBorrowers().add(i.getBorrower());
					i.setBorrower(i.getWaitlist().poll());
					i.setDate(getDate());
					updateItem(i);
					//TODO: update users???
					return true;
				}
			} else {
				return false;
			}
		}
	}
	
	public static Account readAccount(String username) {
		synchronized(acctLock) {
			Account a = null;
			FindIterable<Account> query = accountCollection.find(Filters.eq("username", username));
			MongoCursor<Account> it = query.iterator();
			if (it.hasNext()) {
				a = it.next();
			}
		return a;
		}
	}
	
	public static <E> Item readItem(String field, E value) {
		synchronized(itemLock) {
			Item i = null;
			FindIterable<Item> query = itemCollection.find(Filters.eq(field, value));
			MongoCursor<Item> it = query.iterator();
			if (it.hasNext()) {
				i = it.next();
			}
			return i;
		}
	}
	
	public static void updateItem(Item i) {
		synchronized (itemLock) {
			Bson filter = Filters.eq("_id", i.getId());
			Bson update = Updates.combine(
					Updates.set("available", i.isAvailable()),
					Updates.set("borrower", i.getBorrower()),
					Updates.set("previousBorrowers", i.getPreviousBorrowers()),
					Updates.set("reviews", i.getReviews()),
					Updates.set("waitlist", i.getWaitlist()),
					Updates.set("date", i.getDate())
			);
			
			UpdateResult ur = itemCollection.updateOne(filter, update);
			System.out.print("updated (" + Integer.toString(i.getId()) + ") " + i.getName() + " ");
			System.out.print(ur.getModifiedCount());
			System.out.println(" time");
		}
	}

	private static void updateAccount(Account a) {
		synchronized(acctLock) {
			Bson filter = Filters.eq("username", a.getUsername());
			Bson update = Updates.set("admin", a.getAdmin());
			accountCollection.updateOne(filter, update);
		}
	}
	
	public static ArrayList<Item> readAllItems(){
		synchronized(itemLock) {
			FindIterable<Item> query = itemCollection.find();
			MongoCursor<Item> it = query.iterator();
			ArrayList<Item> list = new ArrayList<>();
			while (it.hasNext()) {
				list.add(it.next());
			}
			return list;
		}
	}

	public static String mod(String user) {
		synchronized(acctLock) {
			Account a = readAccount(user);
			if (a != null) {
				a.setAdmin(true);
				updateAccount(a);
				return null;
			} else {
				return "Account " + user + " doesn't exist!";
			}
		}
	}
}












