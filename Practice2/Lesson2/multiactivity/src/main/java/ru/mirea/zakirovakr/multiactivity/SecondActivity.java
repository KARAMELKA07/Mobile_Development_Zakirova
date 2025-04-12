package ru.mirea.zakirovakr.multiactivity;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ru.mirea.zakirovakr.multiactivity.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textView = findViewById(R.id.textView2);
        String text = getIntent().getStringExtra("key");
        textView.setText(text);

        Log.i("TestSecondActivity", "onCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TestSecondActivity", "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TestSecondActivity", "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("TestSecondActivity", "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("TestSecondActivity", "onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("TestSecondActivity", "onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TestSecondActivity", "onDestroy()");
    }
}
