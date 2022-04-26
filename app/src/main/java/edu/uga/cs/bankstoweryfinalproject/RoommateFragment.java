package edu.uga.cs.bankstoweryfinalproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoommateFragment} factory method to
 * create an instance of this fragment.
 */
public class RoommateFragment extends ListFragment {

    private List<User> users;
    private DatabaseReference shoppingRef;
    private static final String DEBUG_TAG = "RoommateFragmentDebug";


    public RoommateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoppingRef = FirebaseDatabase.getInstance().getReference();

        //Used to fill in a list of all the rommates and their total purchased prices in the money breakdown.
        users = new ArrayList<>();
        shoppingRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    users.add(user);
                }

                setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1,
                        users));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(DEBUG_TAG, "Error reading the database.");
            }
        });
    }



    /**
     * actions when the view is created
     * @param view the view of the fragment
     * @param savedInstanceState previous state of the fragment
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
    }
}