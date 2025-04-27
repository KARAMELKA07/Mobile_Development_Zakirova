package ru.mirea.zakirovakr.securesharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {

    private TextView textViewPoetName;
    private EditText editTextPoetName;
    private SharedPreferences secureSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPoetName = findViewById(R.id.textViewPoetName);
        editTextPoetName = findViewById(R.id.editTextPoetName);
        Button buttonSave = findViewById(R.id.buttonSave);

        try {
            String mainKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            secureSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    mainKeyAlias,
                    getBaseContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        loadSavedPoetName();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePoetName();
            }
        });
    }

    private void savePoetName() {
        String poetName = editTextPoetName.getText().toString();
        try {
            secureSharedPreferences.edit().putString("POET_NAME", poetName).apply();
            textViewPoetName.setText(poetName.isEmpty() ? "Александр Пушкин" : poetName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadSavedPoetName() {
        String poetName = secureSharedPreferences.getString("POET_NAME", "Александр Пушкин");
        textViewPoetName.setText(poetName);
        editTextPoetName.setText(poetName);
    }
}