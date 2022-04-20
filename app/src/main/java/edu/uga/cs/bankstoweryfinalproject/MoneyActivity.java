package edu.uga.cs.bankstoweryfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.ListFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MoneyActivity extends AppCompatActivity {


    private TextView totalCost;
    private float totalCostFloat;
    private TextView costPer;

    private Button button;

    private static final String DEBUG_TAG = "MoneyActivityDebug";

    private DatabaseReference shoppingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        shoppingRef = FirebaseDatabase.getInstance().getReference();
        totalCostFloat = 0f;

        totalCost = findViewById(R.id.totalCost);
        costPer = findViewById(R.id.CostPerRoomie);

        button = findViewById(R.id.moneyBreakdownButton);
        button.setOnClickListener(new DebtButtonClickListener());

        calculateTotalPrice();


        //action bar to enable to back button
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void calculateTotalPrice() {
        shoppingRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    totalCostFloat = totalCostFloat + user.totalPurchased;
                }

                totalCost.setText("Total Cost: " + String.format("%.2f", totalCostFloat));
                costPer.setText("Cost Per Roommate: " + String.format("%.2f", (totalCostFloat / snapshot.getChildrenCount())));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(DEBUG_TAG, "Error reading the database.");
            }
        });
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