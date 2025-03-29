package ru.mirea.zakirovakr.control_lesson1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView myTextView = findViewById(R.id.textView2);
        if (myTextView != null) {
            myTextView.setText("New text in MIREA");
        }

        Button myButton = findViewById(R.id.button);
        if (myButton != null) {
            myButton.setText("New Button Text");
        }

        CheckBox myCheckBox = findViewById(R.id.checkBox);
        if (myCheckBox != null) {
            myCheckBox.setChecked(true);
        }


    }
}