package edu.uga.cs.bankstoweryfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * New user registration activity.
 */
public class RegisterActivity extends AppCompatActivity {

    private Button register;
    private EditText username;
    private EditText password;
    private EditText email;

    private DatabaseHelper databaseHelper;

    private static final String DEBUG_TAG = "RegisterActivityDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.checkRegisterButton);
        username = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword2);
        email = findViewById(R.id.editTextTextEmailAddress);

        databaseHelper = new DatabaseHelper();
        register.setOnClickListener(new RegisterButtonClickListener());
    }

    /**
     * Button initiates new user registration.
     */
    private class RegisterButtonClickListener implements
            View.OnClickListener {

        @Override
        public void onClick(View view) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            String usernameText = username.getText().toString();
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();

            Log.d(DEBUG_TAG, "username: " + usernameText);
            Log.d(DEBUG_TAG, "email: " + emailText);
            Log.d(DEBUG_TAG, "password: " + passwordText);

            if (!usernameText.equals("") && !emailText.equals("") && !passwordText.equals("")) {

                ValueEventListener nameListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.child("users").child(usernameText).getValue(User.class);
                        if (user == null) { //Check if the username has been taken
                            User newUser = new User(usernameText);
                            firebaseAuth.createUserWithEmailAndPassword(emailText, passwordText)
                                    .addOnCompleteListener(RegisterActivity.this, new SignUpCompleteListener<>(newUser));
                        } else {
                            Log.d(DEBUG_TAG, user.name + " name is already taken.");
                            Toast.makeText(getApplicationContext(), "Name is already taken.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w(DEBUG_TAG, "nameListener:onCancelled", error.toException());
                    }
                };
                databaseHelper.dr.addListenerForSingleValueEvent(nameListener);

            } else {
                Log.d(DEBUG_TAG, "Fields must not be empty.");
                Toast.makeText(getApplicationContext(), "Fields must not be empty.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Checks if the account creation process is complete.
     * @param <Auth> FirebaseAuth instance.
     */
    public class SignUpCompleteListener<Auth> implements OnCompleteListener<Auth> {

        User newUser;

        /**
         * Constructor that takes the new user.
         * @param newUser The user to add to the database.
         */
        public SignUpCompleteListener(User newUser) {
            this.newUser = newUser;
        }

        @Override
        public void onComplete(@NonNull Task<Auth> task) {
            if (task.isSuccessful()) {
                Log.d(DEBUG_TAG, "Sign up successful");
                databaseHelper.createNewUser(newUser);

                //Set the display name for the current user
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(newUser.name).build();

                currentUser.updateProfile(userProfileChangeRequest).addOnCompleteListener(task1 -> {
                    //start home screen activity after display name is set.
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                });
            } else {
                Log.d(DEBUG_TAG, "Sign up failed: " + task.getException());
                Toast.makeText(getApplicationContext(), "Sign up failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}