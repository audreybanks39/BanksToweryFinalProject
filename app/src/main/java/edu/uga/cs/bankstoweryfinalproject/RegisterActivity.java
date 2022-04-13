package edu.uga.cs.bankstoweryfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button register;
    private EditText username;
    private EditText password;
    private EditText email;

    private DatabaseHelper dr;

    private static final String DEBUG_TAG = "RegisterActivityDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.checkRegisterButton);
        username = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword2);
        email = findViewById(R.id.editTextTextEmailAddress);

        dr = new DatabaseHelper();
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
                if (dr.usernameExists(usernameText)) {
                    Log.d(DEBUG_TAG, "That username already exists");
                } else {
                    User newUser = new User(usernameText);
                    firebaseAuth.createUserWithEmailAndPassword(emailText, passwordText)
                            .addOnCompleteListener(RegisterActivity.this, new SignUpCompleteListener<>(newUser));
                }
            } else {
                Log.d(DEBUG_TAG, "Fields must not be empty.");
                //Make toast here
            }
        }
    }

    public class SignUpCompleteListener<Auth> implements OnCompleteListener<Auth> {

        User newUser;

        public SignUpCompleteListener(User newUser) {
            this.newUser = newUser;
        }

        @Override
        public void onComplete(@NonNull Task<Auth> task) {
            if (task.isSuccessful()) {
                Log.d(DEBUG_TAG, "Sign up successful");
                dr.createNewUser(newUser);
            } else {
                Log.d(DEBUG_TAG, "Sign up failed: " + task.getException());
                //Sign up failed
            }
        }
    }
}