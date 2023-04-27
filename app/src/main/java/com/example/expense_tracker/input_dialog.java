package com.example.expense_tracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

public class input_dialog extends AppCompatDialogFragment {
    EditText debt_name;
    EditText debt_amount;
    ExampleDailogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("Details")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name_debt = debt_name.getText().toString();
                        int amount_debt = 0;
                        try {
                            String amount = debt_amount.getText().toString();
                            amount_debt = Integer.parseInt(amount);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        listener.updatetext(name_debt, amount_debt);
                    }
                });

        debt_name = view.findViewById(R.id.debt_person);
        debt_amount = view.findViewById(R.id.debt_amount);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDailogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ "must implement ExampleDailogListener");

        }
    }

    public interface ExampleDailogListener{
        void updatetext(String name, int amount);

    }
}
