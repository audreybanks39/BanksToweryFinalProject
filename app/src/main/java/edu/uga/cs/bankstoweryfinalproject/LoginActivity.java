package edu.uga.cs.bankstoweryfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.checkLoginButton);
        login.setOnClickListener(new LoginButtonClickListener());
    }

    /**
     * Button that lets the users take a quiz.
     */
    private class LoginButtonClickListener implements
            View.OnClickListener {
        /**
         * onCLick function for our overview button
         * takes us to the overview page, passing along the spinner selection
         *
         * @param view
         */
        @Override
        public void onClick(View view) {
            Intent intent = new
                    Intent(view.getContext(),
                    HomeActivity.class);
            email = findViewById(R.id.userText).toString();
            password = findViewById(R.id.passwordText).toString();
            startActivity(intent);
        }
    }
}