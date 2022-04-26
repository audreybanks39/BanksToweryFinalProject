package edu.uga.cs.bankstoweryfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Activity that holds the shopping list.
 */
public class GroceryListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<ShoppingItem> list;
    private ArrayAdapter<ShoppingItem> adapter;
    private ArrayList<Integer> checkItemPositions;

    private Button add;
    private Button delete;
    private Button purchased;

    private DatabaseReference shoppingRef;
    private FirebaseUser currentUser;

    private static final String DEBUG_TAG = "GroceryListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        shoppingRef = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //Get saved checked item positions
        if (savedInstanceState != null) {
            checkItemPositions = savedInstanceState.getIntegerArrayList("itemPositions");
        }

        //action bar to enable to back button
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setting up buttons
        add = findViewById(R.id.addButton);
        add.setOnClickListener(new AddButtonClickListener());
        delete = findViewById(R.id.deleteButton);
        delete.setOnClickListener(new DeleteShoppingItemListener());
        purchased = findViewById(R.id.purchasedButton);
        purchased.setOnClickListener(new MarkAsPurchasedListener());

        listView = findViewById(R.id.listContainer);
        list = new ArrayList<>();

        //get list from database and update local list
        shoppingRef.child("shoppingList").addListenerForSingleValueEvent(initializeShoppingList());
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
        } else if (id == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Button that lets user add an item to the grocery list.
     */
    private class AddButtonClickListener implements
            View.OnClickListener {
        /**
         * onCLick function for our add button
         * brings up a dialog box
         *
         * @param view
         */
        @Override
        public void onClick(View view) {
            DialogFragment newFragment = new AddItemFragment();
            newFragment.show(getSupportFragmentManager(), null);
        }
    }

    /**
     * Button that adds checked items to the purchased list.
     */
    private class MarkAsPurchasedListener implements
            View.OnClickListener {

        @Override
        public void onClick(View view) {
            //Check if any items are checked before continuing
            if (listView.getCheckedItemCount() == 0) {
                Toast.makeText(getApplicationContext(), "No Items Selected", Toast.LENGTH_SHORT).show();
                return;
            }
            DialogFragment newFragment = new PurchaseItemFragment();
            newFragment.show(getSupportFragmentManager(), null);
        }
    }

    /**
     * Button listener to remove items from the shopping list.
     */
    private class DeleteShoppingItemListener implements  View.OnClickListener {

        @Override
        public void onClick(View view) {
            //Check if any items are checked before continuing
            if (listView.getCheckedItemCount() == 0) {
                Toast.makeText(getApplicationContext(), "No Items Selected", Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<ShoppingItem> deleteList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (listView.isItemChecked(i)) {
                    deleteList.add(list.get(i));
                    listView.setItemChecked(i, false);
                }
            }

            for (int i = 0; i < deleteList.size(); i++) {
                removeShoppingItem(deleteList.get(i));
            }
        }
    }

    /**
     * Adds item to the purchased item database.
     * @param item ShoppingItem to add.
     */
    private void addPurchasedItem(ShoppingItem item) {
        item.id = shoppingRef.child("purchasedItems").push().getKey();
        shoppingRef.child("purchasedItems").child(item.id).setValue(item);
    }

    /**
     * Removes item from shopping list and shopping list database.
     * @param item ShoppingItem to remove.
     */
    private void removeShoppingItem(ShoppingItem item) {
        list.remove(item);
        adapter.notifyDataSetChanged();
        shoppingRef.child("shoppingList").child(item.id).removeValue();
    }

    /**
     * Adds a new item to the shopping list in the database and updates the list.
     *
     * @param item the new shopping item to add to the list.
     */
    public void onFinishNewShoppingItemDialog(ShoppingItem item) {
        item.id = shoppingRef.child("shoppingList").push().getKey();
        shoppingRef.child("shoppingList").child(item.getId()).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                list.add(item);
                adapter.notifyDataSetChanged();

                Log.d(DEBUG_TAG, "item saved: " + item);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to create a ShoppingItem for " + item,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Moves the marked items to the purchased list and add the total item price to the current
     * users totalPurchased value.
     * @param price The price of the total items marked.
     */
    public void onFinishPurchaseItemDialog(float price) {
        shoppingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.child("users").child(currentUser.getDisplayName()).getValue(User.class);
                user.totalPurchased = user.totalPurchased + price;
                shoppingRef.child("users").child(currentUser.getDisplayName())
                        .child("totalPurchased").setValue(user.totalPurchased);

                ArrayList<ShoppingItem> purchasedList = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    if (listView.isItemChecked(i)) {
                        purchasedList.add(list.get(i));
                        listView.setItemChecked(i, false);
                    }
                }

                PurchasedGroup purchasedGroup = new PurchasedGroup();
                purchasedGroup.setShoppingItems(purchasedList);
                purchasedGroup.setTotalPrice(price);
                purchasedGroup.setPurchasedUser(currentUser.getDisplayName());

                for (int i = 0; i < purchasedList.size(); i++) {
                    removeShoppingItem(purchasedList.get(i));
                }

                purchasedGroup.id = shoppingRef.child("purchasedItems").push().getKey();
                shoppingRef.child("purchasedItems").child(purchasedGroup.id).setValue(purchasedGroup);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(DEBUG_TAG, "Error reading the database.");
            }
        });
    }

    /**
     * Finish edit dialog listener that edits the name of the selected item.
     * @param pos the list position of the item to edit.
     * @param itemName the new name of the item.
     */
    public void onFinishEditItemDialog(int pos, String itemName) {
        list.get(pos).setItem(itemName);
        adapter.notifyDataSetChanged();

        shoppingRef.child("shoppingList").child(list.get(pos).id).child("item")
                .setValue(list.get(pos).item);
    }

    /**
     * Creates ValueEventListener that initializes the shopping list.
     * @return ValueEventListener.
     */
    private ValueEventListener initializeShoppingList() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    ShoppingItem shoppingItem = dataSnapshot.getValue(ShoppingItem.class);
                    list.add(shoppingItem);
                    Log.d(DEBUG_TAG, "item added: " + shoppingItem.item);
                }

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
                        DialogFragment newFragment = EditItemFragment.newInstance(pos, list.get(pos).item);
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