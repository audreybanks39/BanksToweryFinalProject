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

public class RegisterActivity extends AppCompatActivity {

    private Button register;
    private EditText username;
    private EditText password;
    private EditText email;

    private static final String DEBUG_TAG = "RegisterActivityDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.checkRegisterButton);
        username = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword2);
        email = findViewById(R.id.editTextTextEmailAddress);

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

            String usernameText = username.toString();
            String emailText = email.toString();
            String passwordText = password.toString();

            if (!usernameText.equals("") && !emailText.equals("") && !passwordText.equals("")) {
                User newUser = new User(emailText, usernameText);
                firebaseAuth.createUserWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(RegisterActivity.this, new SignUpCompleteListener<>());
            } else {
                Log.d(DEBUG_TAG, "Fields must not be empty.");
                //Make toast here
            }
        }
    }

    public class SignUpCompleteListener<Auth> implements OnCompleteListener<Auth> {

        @Override
        public void onComplete(@NonNull Task<Auth> task) {
            if (task.isSuccessful()) {
                Log.d(DEBUG_TAG, "Sign up successful");

            } else {
                Log.d(DEBUG_TAG, "Sign up failed");
                //Sign up failed
            }
        }
    }
}