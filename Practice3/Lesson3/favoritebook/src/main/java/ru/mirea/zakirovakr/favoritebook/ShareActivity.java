package ru.mirea.zakirovakr.favoritebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ShareActivity extends AppCompatActivity {

    private EditText editTextBook;
    private EditText editTextQuote;
    private TextView textViewDevBook;
    private TextView textViewDevQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        textViewDevBook = findViewById(R.id.textViewDevBook);
        textViewDevQuote = findViewById(R.id.textViewDevQuote);
        editTextBook = findViewById(R.id.editTextUserBook);
        editTextQuote = findViewById(R.id.editTextUserQuote);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String bookName = extras.getString(MainActivity.BOOK_NAME_KEY);
            String quote = extras.getString(MainActivity.QUOTES_KEY);

            textViewDevBook.setText("Любимая книга разработчика: " + bookName);
            textViewDevQuote.setText("Цитата из книги: " + quote);
        }
    }

    public void sendUserData(View view) {
        String userBook = editTextBook.getText().toString();
        String userQuote = editTextQuote.getText().toString();

        String resultText = "Название Вашей любимой книги: " + userBook + ". Цитата: " + userQuote;

        Intent data = new Intent();
        data.putExtra(MainActivity.USER_MESSAGE, resultText);
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}
