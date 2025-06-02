package ru.mirea.zakirovakr.timeservice;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;

public class ActivityMainBinding implements ViewBinding {
    public final TextView textView;
    public final Button button;
    private final ConstraintLayout rootView;

    private ActivityMainBinding(ConstraintLayout rootView, TextView textView, Button button) {
        this.rootView = rootView;
        this.textView = textView;
        this.button = button;
    }

    public static ActivityMainBinding inflate(LayoutInflater inflater) {
        ConstraintLayout root = (ConstraintLayout) inflater.inflate(R.layout.activity_main, null, false);
        TextView textView = root.findViewById(R.id.textView);
        Button button = root.findViewById(R.id.button);
        return new ActivityMainBinding(root, textView, button);
    }

    public ConstraintLayout getRoot() {
        return rootView;
    }
}
