package ru.mirea.zakirovakr.mireaproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class CameraFragment extends Fragment {

    private ImageView imageView;
    private EditText fioEditText, groupEditText, listNumberEditText;
    private TextView profileInfoTextView;
    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == requireActivity().RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(imageBitmap);
                } else {
                    Toast.makeText(requireContext(), "Не удалось сделать фото", Toast.LENGTH_SHORT).show();
                }
            });
    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    launchCamera();
                } else {
                    Toast.makeText(requireContext(), "Для съемки фото требуется разрешение на использование камеры", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        imageView = view.findViewById(R.id.imageView);
        fioEditText = view.findViewById(R.id.fioEditText);
        groupEditText = view.findViewById(R.id.groupEditText);
        listNumberEditText = view.findViewById(R.id.listNumberEditText);
        profileInfoTextView = view.findViewById(R.id.profileInfoTextView);
        Button captureButton = view.findViewById(R.id.captureButton);
        Button saveButton = view.findViewById(R.id.saveButton);

        captureButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                launchCamera();
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        saveButton.setOnClickListener(v -> {
            String fio = fioEditText.getText().toString().trim();
            String group = groupEditText.getText().toString().trim();
            String listNumber = listNumberEditText.getText().toString().trim();

            if (fio.isEmpty() || group.isEmpty() || listNumber.isEmpty()) {
                Toast.makeText(requireContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            } else {
                String profileInfo = "ФИО: " + fio + "\n" +
                        "Группа: " + group + "\n" +
                        "Номер в списке: " + listNumber;
                profileInfoTextView.setText(profileInfo);
                Toast.makeText(requireContext(), "Профиль сохранен", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(takePictureIntent);
    }
}