package data.datatype;

import java.util.List;
import java.util.ArrayList;

public class Account extends DataType {
    private static final long serialVersionUID = 4243510174987051376L;
	private String username;
    private String password;
    private List<Item> checkedItems;
    private boolean admin;

    public Account() {
        this.checkedItems = new ArrayList<Item>();
        password = null;
    }
    
    public void setAdmin(boolean a) {
    	admin = a;
    }
    
    public boolean getAdmin() {
    	return admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Item> getCheckedItems() {
        return checkedItems;
    }

    public void setCheckedItems(List<Item> checkedItems) {
        this.checkedItems = checkedItems;
    }
    
    @Override
    public boolean equals(Object other) {
    	return ((Account)other).getUsername().equals(this.username);
    }
}
