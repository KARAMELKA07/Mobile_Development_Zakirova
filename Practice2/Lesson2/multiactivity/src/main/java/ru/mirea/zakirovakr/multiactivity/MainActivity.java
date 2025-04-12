package ru.mirea.zakirovakr.multiactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private Button buttonSend;
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

        editText = findViewById(R.id.editText);
        buttonSend = findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(v -> {
            String text = editText.getText().toString();
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("key", text);
            startActivity(intent);
        });

        Log.i("TestMainActivity", "onCreate()");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TestMainActivity", "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TestMainActivity", "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("TestMainActivity", "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("TestMainActivity", "onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("TestMainActivity", "onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TestMainActivity", "onDestroy()");
    }


}