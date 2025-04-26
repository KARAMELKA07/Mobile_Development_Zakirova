package ru.mirea.zakirovakr.serviceapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class PlayerService extends Service {
    private MediaPlayer mediaPlayer;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static final String TAG = "PlayerService";

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: Initializing PlayerService");

        // Создание уведомления
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText("Playing 'Sunset Dreams' by Alex Rivers")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Music Player Service - Enjoy your music!"))
                .setContentTitle("Music Player");

        // Создание канала уведомлений
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Student FIO Notification", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("MIREA Channel");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.createNotificationChannel(channel);

        // Запуск сервиса в foreground режиме
        try {
            startForeground(1, builder.build());
            Log.d(TAG, "onCreate: Foreground service started successfully");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Failed to start foreground service", e);
            stopSelf();
            return;
        }

        // Инициализация MediaPlayer
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.song);
            if (mediaPlayer == null) {
                Log.e(TAG, "onCreate: Failed to create MediaPlayer - resource not found or invalid");
                stopSelf();
                return;
            }
            mediaPlayer.setLooping(false);
            Log.d(TAG, "onCreate: MediaPlayer initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Error initializing MediaPlayer", e);
            stopSelf();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: Starting playback");

        if (mediaPlayer == null) {
            Log.e(TAG, "onStartCommand: MediaPlayer is null, cannot start playback");
            stopSelf();
            return START_NOT_STICKY;
        }

        try {
            mediaPlayer.start();
            Log.d(TAG, "onStartCommand: Playback started successfully");

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    Log.d(TAG, "onCompletion: Playback completed");
                    stopForeground(true);
                    stopSelf();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "onStartCommand: Error starting playback", e);
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: Stopping PlayerService");

        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                mediaPlayer = null;
                Log.d(TAG, "onDestroy: MediaPlayer released");
            }
        } catch (Exception e) {
            Log.e(TAG, "onDestroy: Error releasing MediaPlayer", e);
        }

        stopForeground(true);
        super.onDestroy();
    }
}