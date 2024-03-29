package edu.uga.cs.bankstoweryfinalproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditItemFragment extends DialogFragment {

    private EditText item;
    private static String ITEM_POS = "ITEM_POS";
    private static String ITEM_NAME = "ITEM_NAME";
    private int pos;
    private String name;

    public EditItemFragment() {
        // Required empty public constructor
    }

    public static EditItemFragment newInstance(int pos, String name) {
        EditItemFragment editItemFragment = new EditItemFragment();
        Bundle args = new Bundle();
        args.putInt(ITEM_POS, pos);
        args.putString(ITEM_NAME, name);
        editItemFragment.setArguments(args);
        return editItemFragment;
    }

    public interface EditItemDialogListener {
        void onFinishEditItemDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        pos = getArguments().getInt(ITEM_POS);
        name = getArguments().getString(ITEM_NAME);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.fragment_edit_item,
                (ViewGroup) getActivity().findViewById(R.id.frameLayout));

        item = layout.findViewById(R.id.editTextItemName);
        item.setText(name);

        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);

        // Set the title of the AlertDialog
        builder.setTitle("Edit Grocery Item");
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // close the dialog
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("SAVE", new EditItemFragment.EditButtonClickListener());

        // Create the AlertDialog and show it
        return builder.create();
    }

    private class EditButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String name = item.getText().toString();

            GroceryListActivity itemDialogListener = (GroceryListActivity) getActivity();
            itemDialogListener.onFinishEditItemDialog(pos, name);

            dismiss();
        }
    }

}