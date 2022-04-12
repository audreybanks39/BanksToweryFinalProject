package edu.uga.cs.bankstoweryfinalproject;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class ShoppingList {

    public List<String> items;

    public ShoppingList() {
        //Default Constructor used by the Firebase database.
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
