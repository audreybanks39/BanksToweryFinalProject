package edu.uga.cs.bankstoweryfinalproject;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * POJO class to represent the list of purchased items.
 */
@IgnoreExtraProperties
public class PurchasedItems {

    public List<String> items;
    public float totalCost;

    public PurchasedItems() {
        //Default Constructor used by the Firebase database.
    }

    public PurchasedItems(List<String> items) {
        this.items = items;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }
}
