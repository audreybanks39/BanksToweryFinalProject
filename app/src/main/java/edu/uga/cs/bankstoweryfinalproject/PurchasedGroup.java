package edu.uga.cs.bankstoweryfinalproject;

import java.util.List;

public class PurchasedGroup {

    public float totalPrice;
    public List<ShoppingItem> shoppingItems;
    public String purchasedUser;
    public String id;


    public PurchasedGroup() {
    }

    public String getId() {
        return id;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public List<ShoppingItem> getShoppingItems() {
        return shoppingItems;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setShoppingItems(List<ShoppingItem> shoppingItems) {
        this.shoppingItems = shoppingItems;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPurchasedUser() {
        return purchasedUser;
    }

    public void setPurchasedUser(String purchasedUser) {
        this.purchasedUser = purchasedUser;
    }

    @Override
    public String toString() {
        String group = "";
        for (int i = 0; i < shoppingItems.size(); i++) {
            group = group + shoppingItems.get(i).item + "\n";
        }

        group = group + "$" + totalPrice;
        return group;
    }
}
