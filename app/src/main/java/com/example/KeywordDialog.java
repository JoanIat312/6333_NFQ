package com.example;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nfq.R;

public class KeywordDialog extends AppCompatDialogFragment {
    private keywordDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View keyInputView = inflater.inflate(R.layout.keyword_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(keyInputView);

        final EditText mEtKey = (EditText) keyInputView.findViewById(R.id.keyword);
        final EditText mEtDefinition = (EditText) keyInputView.findViewById(R.id.definition);

        Button btnAdd1 = (Button) keyInputView.findViewById(R.id.save_button);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setTitle("Insert Term into Note");
               /* .setNegativeButton("Cancel", null)
                // Add action buttons
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        //listener.onDialogPositiveClick(KeywordDialog.this);
                        listener.applyKeyword(mEtKey.getText().toString(), mEtDefinition.getText().toString());
                    }
                });*/

        btnAdd1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //listener.applyKeyword(mEtKey.getText().toString(), mEtDefinition.getText().toString());
                // btnAdd1 has been clicked
                Log.d("STATE", mEtKey.getText().toString() + " " + mEtDefinition.getText().toString());
                dismiss();

            }
        });


        return builder.create();

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (keywordDialogListener) listener;
        } catch (ClassCastException e) {
            throw new ClassCastException("Must implement dialog listener");
        }
    }

    public interface keywordDialogListener{
        void applyKeyword(String key, String def);
        /*void onDialogPositiveClick(DialogFragment dialog);*/
    }
}
