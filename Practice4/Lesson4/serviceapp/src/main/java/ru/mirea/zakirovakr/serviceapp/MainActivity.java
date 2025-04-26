package ru.mirea.zakirovakr.serviceapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private static final int NOTIFICATION_PERMISSION_CODE = 100;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Проверка разрешения на уведомления
        checkNotificationPermission();

        // Инициализация кнопок
        Button buttonPlay = findViewById(R.id.buttonPlay);
        Button buttonStop = findViewById(R.id.buttonStop);

        // Обработчик для кнопки Play
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Play button clicked");
                Intent serviceIntent = new Intent(MainActivity.this, PlayerService.class);
                try {
                    startForegroundService(serviceIntent);
                    Toast.makeText(MainActivity.this, "Starting music playback", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e(TAG, "Error starting service", e);
                    Toast.makeText(MainActivity.this, "Failed to start playback: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Обработчик для кнопки Stop
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Stop button clicked");
                Intent serviceIntent = new Intent(MainActivity.this, PlayerService.class);
                try {
                    stopService(serviceIntent);
                    Toast.makeText(MainActivity.this, "Stopping music playback", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e(TAG, "Error stopping service", e);
                    Toast.makeText(MainActivity.this, "Failed to stop playback: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Метод для проверки разрешения на уведомления
    private void checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    NOTIFICATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}