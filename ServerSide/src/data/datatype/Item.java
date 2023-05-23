package data.datatype;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class Item extends DataType {
	private static final long serialVersionUID = 5674936882952447809L;
	private static int idct = 0;
	private int id;
	private ItemType type;
	private String name;
	private String description;
	private String date;
	private byte[] image;
	private boolean available;
	private String borrower;
	private ArrayList<String> previousBorrowers;
	private ArrayList<String> reviews;
	
	private ArrayDeque<String> waitlist;
	
	public Item() {
		available = true;
		borrower = "";
		date = "";
		description = null;
		previousBorrowers = new ArrayList<>();
		reviews = new ArrayList<>();
		waitlist = new ArrayDeque<>();
		id = idct++;
		this.name = "";
		image = new byte[0];
		this.type = null;
	}
	
	public Item(ItemType type) {
		this();
		this.type = type;
	}
	
	public Item(String name, ItemType type){
		this();
		this.name = name;
		this.type = type;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public ArrayDeque<String> getWaitlist(){
		return waitlist;
	}
	
	public void setWaitlist(ArrayDeque<String> q) {
		waitlist = q;
	}
	
	public ArrayList<String> getReviews(){
		return reviews;
	}
	
	public void setReviews(ArrayList<String> r) {
		reviews = r;
	}
	
	public static String[] parseReview(String review) {
		String[] res = new String[2];
		res[0] = review.substring(0, review.indexOf('\\'));
		res[1] = review.substring(review.indexOf('\\')+1);
		return res;
	}
	
	public void writeReview(String author, String review) {
		this.reviews.add(author + "\\" + review);
	}
	
	public byte[] getImage() {
		return image;
	}
	
	public void setImage(byte[] bytes) {
		image = bytes;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setDescription(String description) {
    	this.description = description;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public ArrayList<String> getPreviousBorrowers(){
    	return previousBorrowers;
    }
    
    public void setPreviousBorrowers(ArrayList<String> previousBorrowers) {
    	this.previousBorrowers = previousBorrowers;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    @Override
	public boolean equals(Object other) {
		return ((Item)other).getId() == this.id;
	}
	
	public static enum ItemType {
		
		BOOK(0),
		AUDIOBOOK(1),
		DVD(2),
		GAME(3);
		
		
		private Integer intVal;
		
		private static String[] namemap = new String[]{
			"Book",
			"Audiobook",
			"DVD",
			"Game"
		};

		ItemType(final int i) {
			intVal = i;
		}
		
		public Integer getIntValue() {
			return intVal;
		}
		
		public String getName() {
			return namemap[getIntValue()];
		}
		
	}
}

