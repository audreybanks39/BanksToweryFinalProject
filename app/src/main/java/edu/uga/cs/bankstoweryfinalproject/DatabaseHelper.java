package edu.uga.cs.bankstoweryfinalproject;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseHelper {

    private DatabaseReference dr;
    private FirebaseAuth fa;

    public DatabaseHelper() {
        dr = FirebaseDatabase.getInstance().getReference();
        fa = FirebaseAuth.getInstance();
    }

//    private void createNewUser(String name, String email, String password) {
//        User newUser = new User(email, name);
//        fa.createUserWithEmailAndPassword(email, password).addOnCompleteListener()
//    }

}
