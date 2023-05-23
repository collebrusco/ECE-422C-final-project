package network.messages;

public class ReviewMessage extends Message {

	private static final long serialVersionUID = -2676553389544911793L;
	private String review;
	private int itemID;
	private String author;
	boolean success;
	
	public ReviewMessage(int i, String r, String a) {
		itemID = i;
		review = r;
		author = a;
	}
	
	public void setItemID(int i) {
		this.itemID = i;
	}
	
	public int getItemID() {
		return itemID;
	}

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
