package edu.uga.cs.bankstoweryfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {

    private Button register;
    private String name;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.checkRegisterButton);
        register.setOnClickListener(new RegisterButtonClickListener());
    }

    /**
     * Button that lets the users take a quiz.
     */
    private class RegisterButtonClickListener implements
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
                    LoginActivity.class);
            //
            email = findViewById(R.id.editTextTextEmailAddress).toString();
            password = findViewById(R.id.editTextTextPassword2).toString();
            name = findViewById(R.id.editTextTextPersonName).toString();

            startActivity(intent);
        }
    }
}