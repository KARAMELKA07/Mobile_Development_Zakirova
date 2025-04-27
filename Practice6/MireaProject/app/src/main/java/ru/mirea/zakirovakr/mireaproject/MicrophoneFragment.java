package ru.mirea.zakirovakr.mireaproject;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.IOException;

public class MicrophoneFragment extends Fragment {

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private TextView statusTextView;
    private Button startRecordButton, stopRecordButton, playButton;
    private boolean isRecording = false;
    private String audioFilePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_microphone, container, false);

        statusTextView = view.findViewById(R.id.statusTextView);
        startRecordButton = view.findViewById(R.id.startRecordButton);
        stopRecordButton = view.findViewById(R.id.stopRecordButton);
        playButton = view.findViewById(R.id.playButton);

        audioFilePath = requireActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC) + "/audio_record.3gp";

        startRecordButton.setOnClickListener(v -> startRecording());
        stopRecordButton.setOnClickListener(v -> stopRecording());
        playButton.setOnClickListener(v -> playRecording());

        // Initial button states
        stopRecordButton.setEnabled(false);
        playButton.setEnabled(false);

        return view;
    }

    private void startRecording() {
        if (!isRecording) {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                isRecording = true;
                statusTextView.setText("Идёт запись...");
                startRecordButton.setEnabled(false);
                stopRecordButton.setEnabled(true);
                playButton.setEnabled(false);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Не удалось начать запись", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void stopRecording() {
        if (isRecording) {
            try {
                mediaRecorder.stop();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            statusTextView.setText("Запись остановлена. Готово к воспроизведению.");
            startRecordButton.setEnabled(true);
            stopRecordButton.setEnabled(false);
            playButton.setEnabled(true);
        }
    }

    private void playRecording() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            statusTextView.setText("Воспроизведение...");
            playButton.setEnabled(false);
            mediaPlayer.setOnCompletionListener(mp -> {
                statusTextView.setText("Воспроизведение завершено.");
                playButton.setEnabled(true);
                mediaPlayer.release();
                mediaPlayer = null;
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Не удалось воспроизвести запись", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isRecording) {
            stopRecording();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}