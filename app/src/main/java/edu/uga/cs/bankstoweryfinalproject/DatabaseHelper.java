package edu.uga.cs.bankstoweryfinalproject;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseHelper {

    private DatabaseReference dr;
    private static final String DEBUG_TAG = "DatabaseHelperDebug";

    public DatabaseHelper() {
        dr = FirebaseDatabase.getInstance().getReference();
    }

    public void createNewUser(User user) {
        dr.child("users").child(user.name).setValue(user);
    }

    public boolean usernameExists(String name) {
        DoesNameExistListener<DataSnapshot> doesNameExistListener = new DoesNameExistListener<>();
        dr.child("users").child(name).get().addOnCompleteListener(doesNameExistListener);

        return doesNameExistListener.getDoesExist();
    }

    public class DoesNameExistListener<DataSnapShot> implements  OnCompleteListener<DataSnapShot> {
        boolean doesExist;

        private boolean getDoesExist() {
            return doesExist;
        }

        @Override
        public void onComplete(@NonNull Task<DataSnapShot> task) {
            if (task.isSuccessful()) {

            } else {
                doesExist = false;
            }
        }
    }


}
