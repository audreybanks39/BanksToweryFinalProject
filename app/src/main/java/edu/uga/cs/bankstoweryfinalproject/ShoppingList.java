package edu.uga.cs.bankstoweryfinalproject;

import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class ShoppingList {

    public List<String> items;

    public ShoppingList() {
        items = new ArrayList<>();
        //Default Constructor used by the Firebase databases
    }

    public void addItem(String s) {
        items.add(s);
    }

    public String[] getItems() {
        int i = items.size();
        String[] arr = new String[i];
        for (int x = 0; x < i; x ++) {
            arr[x] = items.get(x);
        }
        return arr;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
