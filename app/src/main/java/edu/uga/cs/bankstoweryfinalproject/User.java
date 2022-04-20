package edu.uga.cs.bankstoweryfinalproject;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * POJO class to represent the User.
 */
@IgnoreExtraProperties
public class User {

    public String name;
    public float totalPurchased;

    public User() {
        //Default Constructor used by the Firebase database.
    }

    public User(String name) {
        this.name = name;
        totalPurchased = 0.00f;
    }

    public float getTotalPurchased() {
        return totalPurchased;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalPurchased(float totalPurchased) {
        this.totalPurchased = totalPurchased;
    }

    @Override
    public String toString() {
        return name + "\n$" + totalPurchased;
    }
}
