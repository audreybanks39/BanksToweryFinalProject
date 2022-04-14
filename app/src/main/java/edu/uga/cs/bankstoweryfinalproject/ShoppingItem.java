package edu.uga.cs.bankstoweryfinalproject;

public class ShoppingItem {
    public String item;

    public ShoppingItem() {

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
