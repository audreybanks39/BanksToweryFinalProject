package edu.uga.cs.bankstoweryfinalproject;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button login;
    private Button register;
    private static final String DEBUG_TAG = "MainActivity";

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
            Intent intent = new Intent(view.getContext(), RegisterActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Button that lets the users login.
     */
    private class LoginButtonClickListener implements
            View.OnClickListener {
        /**
         * Launch Firebase UI Sign in screen.
         *
         * @param view
         */
        @Override
        public void onClick(View view) {
            List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());
            Intent signInIntent = AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                    .build();
            signInLauncher.launch(signInIntent);
        }
    }

    private ActivityResultLauncher<Intent> signInLauncher =
            registerForActivityResult(
                    new FirebaseAuthUIActivityResultContract(),
                    new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                        @Override
                        public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                            onSignInResult(result);
                        }
                    }
            );

    /**
     * On successful sign in, launch home screen.
     *
     * @param result
     */
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            if (response != null) {
                Log.d(DEBUG_TAG, "MainActivity.onSignInResult: response.getEmail(): " + response.getEmail());
            }

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            Log.d(DEBUG_TAG, "MainActivity.onSignInResult: Failed to sign in");
            // Sign in failed. If response is null the user canceled the
            Toast.makeText(getApplicationContext(),
                    "Sign in failed",
                    Toast.LENGTH_SHORT).show();
        }
    }
}