package edu.uga.cs.bankstoweryfinalproject;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * POJO class to represent the User.
 */
@IgnoreExtraProperties
public class User {

    public String email;
    public String name;
    public float totalPurchased;

    public User() {
        //Default Constructor used by the Firebase database.
    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public float getTotalPurchased() {
        return totalPurchased;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalPurchased(float totalPurchased) {
        this.totalPurchased = totalPurchased;
    }
}
