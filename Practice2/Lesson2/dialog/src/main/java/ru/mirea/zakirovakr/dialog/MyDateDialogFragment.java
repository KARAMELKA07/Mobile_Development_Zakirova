package ru.mirea.zakirovakr.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class MyDateDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), (view, year1, month1, dayOfMonth) -> {
            Toast.makeText(getActivity(),
                    "Вы выбрали: " + dayOfMonth + "." + (month1 + 1) + "." + year1,
                    Toast.LENGTH_LONG).show();
        }, year, month, day);
    }
}
