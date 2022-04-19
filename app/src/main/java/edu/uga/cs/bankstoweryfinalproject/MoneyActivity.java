package edu.uga.cs.bankstoweryfinalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.ListFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MoneyActivity extends AppCompatActivity {


    private TextView totalCost;
    private TextView costPer;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        totalCost = findViewById(R.id.totalCost);
        costPer = findViewById(R.id.CostPerRoomie);

        button = findViewById(R.id.moneyBreakdownButton);
        button.setOnClickListener(new DebtButtonClickListener());

        //action bar to enable to back button
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    /**
     * takes in the item and uses it to navigate back to the previous page
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            supportNavigateUpTo(new Intent(this, HomeActivity.class));
            return true;
        }
        else if (id == R.id.logout){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Button that lets the users return their money balance to zero.
     */
    private class DebtButtonClickListener implements
            View.OnClickListener {
        /**
         * onCLick function for our debt button
         * clears debts to zero
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