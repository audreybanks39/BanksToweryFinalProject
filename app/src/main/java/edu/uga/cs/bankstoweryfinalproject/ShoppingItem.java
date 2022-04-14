package edu.uga.cs.bankstoweryfinalproject;

public class ShoppingItem {
    public String item;
    public String id;

    public ShoppingItem() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return item;
    }
}
