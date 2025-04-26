package ru.mirea.zakirovakr.pr4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class BackgroundTaskFragment extends Fragment {

    private TextView statusTextView;
    private Button startTaskButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_background_task, container, false);

        statusTextView = view.findViewById(R.id.statusTextView);
        startTaskButton = view.findViewById(R.id.startTaskButton);

        startTaskButton.setOnClickListener(v -> startBackgroundTask());

        return view;
    }

    private void startBackgroundTask() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        WorkRequest taskRequest = new OneTimeWorkRequest.Builder(TaskWorker.class)
                .setConstraints(constraints)
                .build();

        WorkManager workManager = WorkManager.getInstance(requireContext());
        workManager.enqueue(taskRequest);

        workManager.getWorkInfoByIdLiveData(taskRequest.getId()).observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo != null) {
                WorkInfo.State state = workInfo.getState();
                switch (state) {
                    case ENQUEUED:
                        statusTextView.setText("Task Status: Enqueued...");
                        break;
                    case RUNNING:
                        statusTextView.setText("Task Status: Running...");
                        break;
                    case SUCCEEDED:
                        statusTextView.setText("Task Status: Succeeded!");
                        break;
                    case FAILED:
                        statusTextView.setText("Task Status: Failed.");
                        break;
                    case BLOCKED:
                        statusTextView.setText("Task Status: Blocked...");
                        break;
                    case CANCELLED:
                        statusTextView.setText("Task Status: Cancelled.");
                        break;
                }
            }
        });
    }
}