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
 * Use the {@link EditItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditItemFragment extends DialogFragment {

    private EditText item;

    public EditItemFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.fragment_add_item,
                (ViewGroup) getActivity().findViewById(R.id.frameLayout));

        item = layout.findViewById(R.id.editTextItemName);

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

                    // JobLead jobLead = new JobLead( companyName, phone, url, comments );

                    // get the Activity's listener to add the new job lead
                    //AddJobLeadDialogListener listener = (AddJobLeadDialogListener) getActivity();

                    // add the new job lead
                    //listener.onFinishAddJobLeadDialog( jobLead );

                    // close the dialog
                            dismiss();
        }
    }

}