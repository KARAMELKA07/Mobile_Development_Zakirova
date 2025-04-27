package ru.mirea.zakirovakr.internalfilestorage;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private String fileName = "history.txt";
    private EditText editTextDate;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextDate = findViewById(R.id.editTextDate);
        textViewResult = findViewById(R.id.textViewResult);
        Button buttonSave = findViewById(R.id.buttonSave);

        editTextDate.setText("9 мая 1945 года: День Победы в Великой Отечественной войне");

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile();
            }
        });

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5000); // Задержка 5 секунд
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                textViewResult.post(new Runnable() {
                    public void run() {
                        String text = getTextFromFile();
                        textViewResult.setText(text != null ? text : "Файл не найден или пуст");
                    }
                });
            }
        }).start();
    }

    private void saveToFile() {
        String text = editTextDate.getText().toString();
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(text.getBytes());
            outputStream.close();
            Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при сохранении: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getTextFromFile() {
        FileInputStream fin = null;
        try {
            fin = openFileInput(fileName);
            byte[] bytes = new byte[fin.available()];
            int bytesRead = fin.read(bytes);
            if (bytesRead > 0) {
                String text = new String(bytes, 0, bytesRead);
                Log.d(LOG_TAG, "Прочитано из файла: " + text);
                return text;
            } else {
                Log.d(LOG_TAG, "Файл пуст");
                return "Файл пуст";
            }
        } catch (IOException ex) {
            Log.d(LOG_TAG, "Ошибка чтения файла: " + ex.getMessage());
            Toast.makeText(this, "Ошибка чтения: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (IOException ex) {
                Log.d(LOG_TAG, "Ошибка закрытия файла: " + ex.getMessage());
            }
        }
    }
}