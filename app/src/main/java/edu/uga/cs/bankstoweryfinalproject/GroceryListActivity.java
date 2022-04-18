package edu.uga.cs.bankstoweryfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;

public class GroceryListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<ShoppingItem> list;
    private ArrayAdapter<ShoppingItem> adapter;
    //private String[] l1 = {"milk", "eggs", "Coffee"};
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
        ShoppingItem test = new ShoppingItem();

        //get list from database and update local list
        shoppingRef.child("shoppingList").addListenerForSingleValueEvent(initializeShoppingList());
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

                for (int i = 0; i < purchasedList.size(); i++) {
                    removeShoppingItem(purchasedList.get(i));
                    addPurchasedItem(purchasedList.get(i));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Cretes ValueEventListener that initializes the shopping list.
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

                adapter = new ArrayAdapter<>(GroceryListActivity.this, android.R.layout.simple_list_item_multiple_choice,
                        android.R.id.text1, list);

                listView.setAdapter(adapter);

                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                        DialogFragment newFragment = new EditItemFragment();
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
}