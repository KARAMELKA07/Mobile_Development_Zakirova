package ru.mirea.zakirovakr.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Toast;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class MyTimeDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), (view, hourOfDay, minute1) -> {
            Toast.makeText(getActivity(),
                    "Вы выбрали: " + hourOfDay + ":" + String.format("%02d", minute1),
                    Toast.LENGTH_LONG).show();
        }, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }
}
