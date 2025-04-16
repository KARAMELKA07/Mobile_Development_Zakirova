package ru.mirea.zakirovakr.favoritebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShareActivity extends AppCompatActivity {

    private EditText editBookUser, editQuoteUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        TextView textBookDev = findViewById(R.id.textViewDevBook);
        TextView textQuoteDev = findViewById(R.id.textViewDevQuote);
        editBookUser = findViewById(R.id.editTextUserBook);
        editQuoteUser = findViewById(R.id.editTextUserQuote);
        Button sendButton = findViewById(R.id.buttonSendBack);

        // Получение данных от MainActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String book_name = extras.getString(MainActivity.BOOK_NAME_KEY);
            String quotes_name = extras.getString(MainActivity.QUOTES_KEY);
            textBookDev.setText("Любимая книга разработчика: " + book_name);
            textQuoteDev.setText("Цитата из книги: " + quotes_name);
        }

        // Обработка кнопки отправки
        sendButton.setOnClickListener(v -> {
            String book = editBookUser.getText().toString();
            String quote = editQuoteUser.getText().toString();
            String result = "Название Вашей любимой книги: " + book + ". Цитата: " + quote;

            Intent data = new Intent();
            data.putExtra(MainActivity.USER_MESSAGE, result);
            setResult(Activity.RESULT_OK, data);
            finish();
        });
    }
}
