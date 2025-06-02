package ru.mirea.zakirovakr.mireaproject;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SensorFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private TextView directionTextView;

    private float[] gravity;
    private float[] geomagnetic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);

        directionTextView = view.findViewById(R.id.directionTextView);
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values.clone();
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values.clone();
        }

        if (gravity != null && geomagnetic != null) {
            float[] R = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic);
            if (success) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);
                float azimuth = (float) Math.toDegrees(orientation[0]);
                azimuth = (azimuth + 360) % 360;

                String direction;
                if (azimuth >= 337.5 || azimuth < 22.5) {
                    direction = "Север";
                } else if (azimuth >= 22.5 && azimuth < 67.5) {
                    direction = "Северо-восток";
                } else if (azimuth >= 67.5 && azimuth < 112.5) {
                    direction = "Восток";
                } else if (azimuth >= 112.5 && azimuth < 157.5) {
                    direction = "Юго-восток";
                } else if (azimuth >= 157.5 && azimuth < 202.5) {
                    direction = "Юг";
                } else if (azimuth >= 202.5 && azimuth < 247.5) {
                    direction = "Юго-запад";
                } else if (azimuth >= 247.5 && azimuth < 292.5) {
                    direction = "Запад";
                } else {
                    direction = "Северо-запад";
                }

                directionTextView.setText("Направление: " + direction + " (" + String.format("%.1f", azimuth) + "°)");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }
}