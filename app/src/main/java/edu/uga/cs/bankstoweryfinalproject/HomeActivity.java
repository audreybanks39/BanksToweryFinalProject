package edu.uga.cs.bankstoweryfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private Button grocery;
    private Button recentPurchases;
    private Button money;
    private FirebaseUser currentUser;
    private DatabaseHelper databaseHelper;

    private static final String DEBUG_TAG = "HomeActivityDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseHelper = new DatabaseHelper();

        grocery = findViewById(R.id.groceryButton);
        recentPurchases = findViewById(R.id.recentPurchaseButton);
        money = findViewById(R.id.moneyButton);

        grocery.setOnClickListener(new GroceryButtonClickListener());
        recentPurchases.setOnClickListener(new RecentButtonClickListener());
        money.setOnClickListener(new MoneyButtonClickListener());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(DEBUG_TAG, "Current user: " + currentUser.getEmail());
        if (currentUser.getDisplayName() != null) {
            Log.d(DEBUG_TAG, "Current user name: " + currentUser.getDisplayName());
            databaseHelper.dr.addListenerForSingleValueEvent(createNameListener());
        }
    }


    /**
     * Button that lets the users take a quiz.
     */
    private class RecentButtonClickListener implements
            View.OnClickListener {
        /**
         * onCLick function for our overview button
         * takes us to the overview page, passing along the spinner selection
         *
         * @param view
         */
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), RecentPurchaseActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Button that lets the users take a quiz.
     */
    private class GroceryButtonClickListener implements
            View.OnClickListener {
        /**
         * onCLick function for our overview button
         * takes us to the overview page, passing along the spinner selection
         *
         * @param view
         */
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), GroceryListActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Button that lets the users take a quiz.
     */
    private class MoneyButtonClickListener implements
            View.OnClickListener {
        /**
         * onCLick function for our overview button
         * takes us to the overview page, passing along the spinner selection
         *
         * @param view
         */
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), MoneyActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Creates listener to check if a username already exists.
     * Adds a new user with that name if it doesn't exist.
     * @return ValueEventListener.
     */
    private ValueEventListener createNameListener() {
        ValueEventListener nameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.child("users").child(currentUser.getDisplayName()).getValue(User.class);
                if (user == null) { //Check if the user name is saved in the users database.
                    User newUser = new User(currentUser.getDisplayName());
                    databaseHelper.createNewUser(newUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(DEBUG_TAG, "nameListener:onCancelled", error.toException());
            }
        };
        return nameListener;
    }
}