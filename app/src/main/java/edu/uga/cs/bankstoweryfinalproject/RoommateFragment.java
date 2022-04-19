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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoommateFragment} factory method to
 * create an instance of this fragment.
 */
public class RoommateFragment extends ListFragment {

    private List<User> users;


    public RoommateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        users = new ArrayList<>();
        users.add(new User("Emily"));
        users.add(new User("Audrey"));

    }

    /**
     * actions when the view is created
     * @param view the view of the fragment
     * @param savedInstanceState previous state of the fragment
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1,
                users));

        getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
    }
}