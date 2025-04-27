package ru.mirea.zakirovakr.notebook;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSION_CODE = 100;
    private EditText editTextFileName, editTextQuote;
    private TextView textViewStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextFileName = findViewById(R.id.editTextFileName);
        editTextQuote = findViewById(R.id.editTextQuote);
        textViewStatus = findViewById(R.id.textViewStatus);
        Button buttonSave = findViewById(R.id.buttonSave);
        Button buttonLoad = findViewById(R.id.buttonLoad);

        requestStoragePermissions();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuoteToFile();
            }
        });

        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readQuoteFromFile();
            }
        });

        saveInitialQuotes();
    }

    private void requestStoragePermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE);
        }
    }

    private void saveInitialQuotes() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (!path.exists()) {
            path.mkdirs();
        }

        File file1 = new File(path, "einstein_quote.txt");
        try (FileOutputStream fos = new FileOutputStream(file1)) {
            String quote = "Стремись не к тому, чтобы добиться успеха, а к тому, чтобы твоя жизнь имела смысл. — Альберт Эйнштейн";
            fos.write(quote.getBytes(StandardCharsets.UTF_8));
            Log.d(TAG, "Цитата Эйнштейна сохранена");
        } catch (IOException e) {
            Log.e(TAG, "Ошибка сохранения цитаты Эйнштейна: " + e.getMessage());
        }

        File file2 = new File(path, "confucius_quote.txt");
        try (FileOutputStream fos = new FileOutputStream(file2)) {
            String quote = "Выбери себе работу по душе, и тебе не придется работать ни одного дня в своей жизни. — Конфуций";
            fos.write(quote.getBytes(StandardCharsets.UTF_8));
            Log.d(TAG, "Цитата Конфуция сохранена");
        } catch (IOException e) {
            Log.e(TAG, "Ошибка сохранения цитаты Конфуция: " + e.getMessage());
        }
    }

    private void saveQuoteToFile() {
        String fileName = editTextFileName.getText().toString().trim();
        String quote = editTextQuote.getText().toString().trim();

        if (fileName.isEmpty() || quote.isEmpty()) {
            Toast.makeText(this, "Введите название файла и цитату", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!fileName.endsWith(".txt")) {
            fileName += ".txt";
        }

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (!path.exists()) {
            path.mkdirs();
        }

        File file = new File(path, fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(quote.getBytes(StandardCharsets.UTF_8));
            textViewStatus.setText("Файл " + fileName + " сохранён");
            Toast.makeText(this, "Файл сохранён", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            textViewStatus.setText("Ошибка сохранения: " + e.getMessage());
            Log.e(TAG, "Ошибка сохранения файла: " + e.getMessage());
        }
    }

    private void readQuoteFromFile() {
        String fileName = editTextFileName.getText().toString().trim();

        if (fileName.isEmpty()) {
            Toast.makeText(this, "Введите название файла", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!fileName.endsWith(".txt")) {
            fileName += ".txt";
        }

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, fileName);
        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            String quote = String.join("\n", lines);
            editTextQuote.setText(quote);
            textViewStatus.setText("Файл " + fileName + " загружен");
            Log.d(TAG, "Прочитано из файла: " + quote);
        } catch (IOException e) {
            textViewStatus.setText("Ошибка загрузки: " + e.getMessage());
            Toast.makeText(this, "Ошибка загрузки: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Ошибка чтения файла: " + e.getMessage());
        }
    }
}