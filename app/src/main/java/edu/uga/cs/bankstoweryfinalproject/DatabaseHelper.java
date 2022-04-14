package edu.uga.cs.bankstoweryfinalproject;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DatabaseHelper {

    public DatabaseReference dr;
    private static final String DEBUG_TAG = "DatabaseHelperDebug";

    public DatabaseHelper() {
        dr = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Write new user object to the database.
     * @param user User to write to the database.
     */
    public void createNewUser(User user) {
        dr.child("users").child(user.name).setValue(user);
    }

//    public List<String> getShoppingList() {
//
//    }

}
