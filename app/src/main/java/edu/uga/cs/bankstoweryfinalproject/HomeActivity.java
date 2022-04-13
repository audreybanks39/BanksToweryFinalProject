package edu.uga.cs.bankstoweryfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private Button grocery;
    private Button recentPurchases;
    private Button money;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        grocery = findViewById(R.id.groceryButton);
        recentPurchases = findViewById(R.id.recentPurchaseButton);
        money = findViewById(R.id.moneyButton);

        grocery.setOnClickListener(new GroceryButtonClickListener());
        recentPurchases.setOnClickListener(new RecentButtonClickListener());
        money.setOnClickListener(new MoneyButtonClickListener());

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
            Intent intent = new
                    Intent(view.getContext(),
                    RecentPurchaseActivity.class);
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
            Intent intent = new
                    Intent(view.getContext(),
                    GroceryListActivity.class);
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
            Intent intent = new
                    Intent(view.getContext(),
                    MoneyActivity.class);
            startActivity(intent);
        }
    }
}