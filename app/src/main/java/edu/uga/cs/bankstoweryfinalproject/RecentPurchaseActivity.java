package edu.uga.cs.bankstoweryfinalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecentPurchaseActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<PurchasedGroup> list;
    private ArrayList<PurchasedGroup> deleteList;
    private ArrayList<Integer> checkItemPositions;
    private ArrayAdapter<PurchasedGroup> adapter;

    private Button delete;
    private Button settleCost;
    private TextView totalCost;
    private float totalCostFloat;

    private DatabaseReference shoppingRef;
    private FirebaseUser currentUser;

    private static final String DEBUG_TAG = "RecentPurchaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_purchase);
        shoppingRef = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        totalCostFloat = 0f;

        //Get saved checked item positions
        if (savedInstanceState != null) {
            checkItemPositions = savedInstanceState.getIntegerArrayList("itemPositions");
        }

        //action bar to enable to back button
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        delete = findViewById(R.id.deleteButton2);
        delete.setOnClickListener(new DeleteButtonListener());
        settleCost = findViewById(R.id.settleCostButton);
        settleCost.setOnClickListener(new SettleCostButtonListener());
        totalCost = findViewById(R.id.totalCost);

        listView = findViewById(R.id.listContainer2);
        list = new ArrayList<>();

        shoppingRef.child("purchasedItems").addListenerForSingleValueEvent(initializePurchaseList());
    }

    private class DeleteButtonListener implements
            View.OnClickListener {

        @Override
        public void onClick(View view) {
            deleteList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (listView.isItemChecked(i)) {
                    deleteList.add(list.get(i));
                    listView.setItemChecked(i, false);
                }
            }

            removePurchasedItem(0);
        }
    }

    /**
     * Removes the purchased group of item from the purchased list, moves them back to the shopping list,
     * and subtracts the purchase price from the user who marked it off.
     */
    private void removePurchasedItem(int n) {
        if (n < deleteList.size()) {
            shoppingRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    totalCostFloat = totalCostFloat - deleteList.get(n).getTotalPrice();
                    totalCost.setText("Total Cost: " + String.format("%.2f", totalCostFloat));

                    list.remove(deleteList.get(n));
                    adapter.notifyDataSetChanged();

                    shoppingRef.child("purchasedItems").child(deleteList.get(n).getId()).removeValue();

                    for (ShoppingItem item: deleteList.get(n).getShoppingItems()) {
                        shoppingRef.child("shoppingList").child(item.id).setValue(item);
                    }

                    User user = snapshot.child(deleteList.get(n).getPurchasedUser()).getValue(User.class);
                    user.setTotalPurchased(user.getTotalPurchased() - deleteList.get(n).getTotalPrice());

                    shoppingRef.child("users").child(deleteList.get(n).getPurchasedUser())
                            .child("totalPurchased").setValue(user.totalPurchased).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            removePurchasedItem(n + 1);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(DEBUG_TAG, "Error reading the database.");
                }
            });
        }
    }

    private class SettleCostButtonListener implements
            View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), MoneyActivity.class);
            startActivity(intent);
        }
    }

    public void onFinishEditPurchasedItemListener(int pos, float price) {
        list.get(pos).setTotalPrice(price);
        adapter.notifyDataSetChanged();

        shoppingRef.child("purchasedItems").child(list.get(pos).id).child("totalPrice").setValue(price);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    /**
     * takes in the item and uses it to navigate back to the previous page
     *
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
        } else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Creates ValueEventListener that initializes the purchased list.
     * @return ValueEventListener.
     */
    private ValueEventListener initializePurchaseList() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(DEBUG_TAG, "snapshot children: " + snapshot.getChildrenCount());
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    PurchasedGroup purchasedGroup = dataSnapshot.getValue(PurchasedGroup.class);
                    totalCostFloat = totalCostFloat + purchasedGroup.getTotalPrice();
                    list.add(purchasedGroup);
                    Log.d(DEBUG_TAG, "item added.");
                }

                totalCost.setText("Total Cost: $" + String.format("%.2f", totalCostFloat));

                adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, R.id.listTextHolder, list);

                listView.setAdapter(adapter);

                //Set checked items from savedInstance
                if (checkItemPositions != null) {
                    for (int i = 0; i < checkItemPositions.size(); i++) {
                        listView.setItemChecked(checkItemPositions.get(i), true);
                    }
                }

                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                        DialogFragment newFragment = EditPriceFragment.newInstance(pos);
                        newFragment.show(getSupportFragmentManager(), null);
                        return true;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(DEBUG_TAG, "Error reading the database.");
            }
        };
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        checkItemPositions = new ArrayList<>();

        //Save checked item positions
        for (int i = 0; i < list.size(); i++) {
            if (listView.isItemChecked(i)) {
                checkItemPositions.add(i);
            }
        }
        outState.putIntegerArrayList("itemPositions", checkItemPositions);
        Log.d(DEBUG_TAG, "onSaveInstanceState()");
    }
}