package edu.uga.cs.bankstoweryfinalproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditPriceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditPriceFragment extends DialogFragment {

    private EditText item;
    private int pos;

    public EditPriceFragment() {
        // Required empty public constructor
    }
    public interface PurchaseItemDialogListener {
        void onFinishPurchaseItemDialog(float price);
    }

    public static EditPriceFragment newInstance(int pos) {
        EditPriceFragment fragment = new EditPriceFragment();
        Bundle args = new Bundle();
        args.putInt("position", pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pos = getArguments().getInt("position");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.fragment_edit_price,
                (ViewGroup) getActivity().findViewById(R.id.root));

        item = layout.findViewById(R.id.edit);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);

        // Set the title of the AlertDialog
        builder.setTitle("Item Prices");
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // close the dialog
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("SAVE", new EditPriceFragment.EditPriceButtonClickListener());

        // Create the AlertDialog and show it
        return builder.create();
    }

    private class EditPriceButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            float price = Float.parseFloat(item.getText().toString());
            RecentPurchaseActivity itemDialogListener = (RecentPurchaseActivity) getActivity();
            itemDialogListener.onFinishEditPurchasedItemListener(pos, price);

            dismiss();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_price, container, false);
    }
}