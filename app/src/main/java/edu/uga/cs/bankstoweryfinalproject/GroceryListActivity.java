package edu.uga.cs.bankstoweryfinalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class GroceryListActivity extends AppCompatActivity {

    private ListView listView;
    private ShoppingList list;
    //private String[] l1 = {"milk", "eggs", "Coffee"};
    private Button add;
    private Button delete;
    private Button purchased;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        //action bar to enable to back button
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setting up buttons
        add = findViewById(R.id.addButton);
        add.setOnClickListener(new AddButtonClickListener());
        delete = findViewById(R.id.deleteButton);
        purchased = findViewById(R.id.purchasedButton);

        list = new ShoppingList();
        list.addItem("milk");
        list.addItem("coffee");

        listView = findViewById(R.id.listContainer);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (GroceryListActivity.this,
                        android.R.layout.simple_list_item_multiple_choice,
                        android.R.id.text1, list.getItems());

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

}