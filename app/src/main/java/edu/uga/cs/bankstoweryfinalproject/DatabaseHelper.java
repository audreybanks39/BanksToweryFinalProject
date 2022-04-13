package edu.uga.cs.bankstoweryfinalproject;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatabaseHelper {

    public DatabaseReference dr;
    private static final String DEBUG_TAG = "DatabaseHelperDebug";

    public DatabaseHelper() {
        dr = FirebaseDatabase.getInstance().getReference();
    }

    public void createNewUser(User user) {
        dr.child("users").child(user.name).setValue(user);
    }




}
