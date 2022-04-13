package edu.uga.cs.bankstoweryfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private Button login;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        register = findViewById(R.id.registerButton);
        login = findViewById(R.id.loginButton);

        register.setOnClickListener(new RegisterButtonClickListener());
        login.setOnClickListener(new LoginButtonClickListener());

    }

    /**
     * Button that lets the users register.
     */
    private class RegisterButtonClickListener implements
            View.OnClickListener {
        /**
         * onCLick function for our register button
         * takes us to the register page
         *
         * @param view
         */
        @Override
        public void onClick(View view) {
            Intent intent = new
                    Intent(view.getContext(),
                    RegisterActivity.class);
            startActivity(intent);
        }
    }
    /**
     * Button that lets the users login.
     */
    private class LoginButtonClickListener implements
            View.OnClickListener {
        /**
         * onCLick function for our login button
         * takes us to the login page
         *
         * @param view
         */
        @Override
        public void onClick(View view) {
            Intent intent = new
                    Intent(view.getContext(),
                    HomeActivity.class);
            startActivity(intent);
        }
    }
}