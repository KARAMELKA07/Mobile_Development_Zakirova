package ru.mirea.zakirovakr.mireaproject;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileWorkFragment extends Fragment {

    private static final String TAG = FileWorkFragment.class.getSimpleName();
    private static final int REQUEST_PERMISSION_CODE = 100;
    private TextView textViewFiles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_work, container, false);

        textViewFiles = view.findViewById(R.id.textViewFiles);
        FloatingActionButton fab = view.findViewById(R.id.fab);

        requestStoragePermissions();

        updateFileList();

        fab.setOnClickListener(v -> showFileDialog());

        return view;
    }

    private void requestStoragePermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE);
        }
    }

    private void showFileDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_file_entry);

        EditText editTextFileName = dialog.findViewById(R.id.editTextFileName);
        EditText editTextContent = dialog.findViewById(R.id.editTextContent);
        Button buttonEncrypt = dialog.findViewById(R.id.buttonEncrypt);
        Button buttonDecrypt = dialog.findViewById(R.id.buttonDecrypt);

        buttonEncrypt.setOnClickListener(v -> {
            String fileName = editTextFileName.getText().toString().trim();
            String content = editTextContent.getText().toString().trim();
            if (fileName.isEmpty() || content.isEmpty()) {
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }
            encryptAndSave(fileName, content);
            dialog.dismiss();
        });

        buttonDecrypt.setOnClickListener(v -> {
            String fileName = editTextFileName.getText().toString().trim();
            if (fileName.isEmpty()) {
                Toast.makeText(requireContext(), "Введите название файла", Toast.LENGTH_SHORT).show();
                return;
            }
            String decrypted = decryptFile(fileName);
            editTextContent.setText(decrypted);
        });

        dialog.show();
    }

    private void encryptAndSave(String fileName, String content) {
        if (!fileName.endsWith(".txt")) {
            fileName += ".txt";
        }

        String encrypted = caesarCipher(content, 3); // Шифр Цезаря со сдвигом 3
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (!path.exists()) {
            path.mkdirs();
        }

        File file = new File(path, fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(encrypted.getBytes(StandardCharsets.UTF_8));
            Toast.makeText(requireContext(), "Файл зашифрован и сохранён", Toast.LENGTH_SHORT).show();
            updateFileList();
        } catch (IOException e) {
            Toast.makeText(requireContext(), "Ошибка сохранения: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Ошибка сохранения: " + e.getMessage());
        }
    }

    private String decryptFile(String fileName) {
        if (!fileName.endsWith(".txt")) {
            fileName += ".txt";
        }

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, fileName);
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            String encrypted = String.join("\n", lines);
            String decrypted = caesarCipher(encrypted, -3); // Дешифровка со сдвигом -3
            Toast.makeText(requireContext(), "Файл расшифрован", Toast.LENGTH_SHORT).show();
            return decrypted;
        } catch (IOException e) {
            Toast.makeText(requireContext(), "Ошибка чтения: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Ошибка чтения: " + e.getMessage());
            return "";
        }
    }

    private String caesarCipher(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                result.append((char) ((c - base + shift + 26) % 26 + base));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private void updateFileList() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File[] files = path.listFiles();
        StringBuilder sb = new StringBuilder("Список зашифрованных файлов:\n");
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".txt")) {
                    sb.append(file.getName()).append("\n");
                }
            }
        } else {
            sb.append("Папка пуста");
        }
        textViewFiles.setText(sb.toString());
    }
}