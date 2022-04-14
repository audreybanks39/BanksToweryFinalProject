package edu.uga.cs.bankstoweryfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    private static final String DEBUG_TAG = "GroceryListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        shoppingRef = FirebaseDatabase.getInstance().getReference("shoppingList");


        //action bar to enable to back button
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setting up buttons
        add = findViewById(R.id.addButton);
        add.setOnClickListener(new AddButtonClickListener());
        delete = findViewById(R.id.deleteButton);
        purchased = findViewById(R.id.purchasedButton);

        listView = findViewById(R.id.listContainer);
        list = new ArrayList<>();
        ShoppingItem test = new ShoppingItem();

        //get list from database and update local list
        shoppingRef.addListenerForSingleValueEvent(initializeShoppingList());
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
     * Adds a new item to the shopping list in the database and updates the list.
     *
     * @param item the new shopping item to add to the list.
     */
    public void onFinishNewShoppingItemDialog(ShoppingItem item) {
        shoppingRef = FirebaseDatabase.getInstance().getReference("shoppingList");

        shoppingRef.push().setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                list.add(item);
                adapter.notifyDataSetChanged();

                Log.d(DEBUG_TAG, "Job lead saved: " + item);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to create a Shopping for " + item,
                        Toast.LENGTH_SHORT).show();
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
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //listView.setItemChecked(i, !listView.isItemChecked(i));
                        Log.d(DEBUG_TAG, listView.isItemChecked(i) + "");
//                        SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();
//                        for (int j = 0; j < sparseBooleanArray.size(); j++) {
//                            Log.d(DEBUG_TAG, "" + sparseBooleanArray.get(j));
//                        }
                    }
                });

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