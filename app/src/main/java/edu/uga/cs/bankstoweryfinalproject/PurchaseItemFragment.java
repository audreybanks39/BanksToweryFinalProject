package edu.uga.cs.bankstoweryfinalproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PurchaseItemFragment} factory method to
 * create an instance of this fragment.
 */
public class PurchaseItemFragment extends DialogFragment {

    private EditText item;

    public PurchaseItemFragment() {
        // Required empty public constructor
    }

    public interface PurchaseItemDialogListener {
        void onFinishPurchaseItemDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.fragment_purchase_item,
                (ViewGroup) getActivity().findViewById(R.id.root));

        item = layout.findViewById(R.id.purchaseItemPrice);

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
        builder.setPositiveButton("SAVE", new ButtonClickListener());

        // Create the AlertDialog and show it
        return builder.create();
    }

    private class ButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            float price = Float.parseFloat(item.getText().toString());

            GroceryListActivity itemDialogListener = (GroceryListActivity) getActivity();
            itemDialogListener.onFinishPurchaseItemDialog(price);

            dismiss();
        }
    }

}