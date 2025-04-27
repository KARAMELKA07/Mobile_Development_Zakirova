package ru.mirea.zakirovakr.pr5;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import ru.mirea.zakirovakr.pr5.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final int REQUEST_CODE_PERMISSION = 200;
    private ActivityMainBinding binding;
    private boolean isWork = false;

    // Sensor variables
    private SensorManager sensorManager;
    private Sensor magnetometer;

    // Camera variables
    private Uri imageUri;
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;

    // Microphone variables
    private MediaRecorder mediaRecorder;
    private String recordFilePath;
    private boolean isRecording = false;
    private Timer noiseLevelTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Request permissions
        checkPermissions();

        // Initialize sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Initialize camera
        setupCamera();

        // Initialize microphone
        recordFilePath = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "/noiseleveltest.mp4").getAbsolutePath();
        setupMicrophone();
    }

    private void checkPermissions() {
        int cameraPermissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int audioPermissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (cameraPermissionStatus == PackageManager.PERMISSION_GRANTED &&
                audioPermissionStatus == PackageManager.PERMISSION_GRANTED &&
                storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            isWork = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (!isWork) {
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Sensor: Compass to find north
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        if (isRecording) {
            startNoiseLevelMonitoring();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        stopRecording();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float x = event.values[0];
            float y = event.values[1];
            float angle = (float) Math.toDegrees(Math.atan2(y, x));
            if (angle < 0) {
                angle += 360;
            }
            String direction;
            if (angle >= 315 || angle < 45) {
                direction = "North";
            } else if (angle >= 45 && angle < 135) {
                direction = "East";
            } else if (angle >= 135 && angle < 225) {
                direction = "South";
            } else {
                direction = "West";
            }
            binding.compassDirection.setText("Direction: " + direction + " (" + angle + "°)");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for this task
    }

    // Camera: Profile Card
    private void setupCamera() {
        ActivityResultCallback<ActivityResult> callback = result -> {
            if (result.getResultCode() == RESULT_OK) {
                binding.profileImage.setImageURI(imageUri);
            }
        };
        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                callback
        );

        binding.takePhotoButton.setOnClickListener(v -> {
            if (isWork) {
                try {
                    File photoFile = createImageFile();
                    String authorities = getPackageName() + ".fileprovider";
                    imageUri = FileProvider.getUriForFile(this, authorities, photoFile);
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
                    cameraActivityResultLauncher.launch(cameraIntent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "PROFILE_" + timeStamp + "_";
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDirectory);
    }

    // Microphone: Noise Level Detector
    private void setupMicrophone() {
        binding.recordAudioButton.setOnClickListener(v -> {
            if (isWork) {
                if (!isRecording) {
                    startRecording();
                    binding.recordAudioButton.setText("Stop Recording");
                    startNoiseLevelMonitoring();
                    isRecording = true;
                } else {
                    stopRecording();
                    binding.recordAudioButton.setText("Start Recording");
                    isRecording = false;
                }
            } else {
                Toast.makeText(this, "Microphone permission required", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(recordFilePath);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            Log.e("MainActivity", "prepare() failed: " + e.getMessage());
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
            } catch (RuntimeException e) {
                Log.e("MainActivity", "stopRecording failed: " + e.getMessage());
            } finally {
                mediaRecorder.release();
                mediaRecorder = null;
            }
        }
        if (noiseLevelTimer != null) {
            noiseLevelTimer.cancel();
            noiseLevelTimer = null;
        }
    }

    private void startNoiseLevelMonitoring() {
        noiseLevelTimer = new Timer();
        noiseLevelTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (mediaRecorder != null) {
                    int amplitude = mediaRecorder.getMaxAmplitude();
                    runOnUiThread(() -> {
                        if (amplitude > 10000) {
                            binding.noiseLevel.setText("Noise Level: High (" + amplitude + ")");
                        } else if (amplitude > 5000) {
                            binding.noiseLevel.setText("Noise Level: Medium (" + amplitude + ")");
                        } else {
                            binding.noiseLevel.setText("Noise Level: Low (" + amplitude + ")");
                        }
                    });
                }
            }
        }, 0, 1000); // Update every second
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRecording();
    }
}