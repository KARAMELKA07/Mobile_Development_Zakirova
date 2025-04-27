package ru.mirea.zakirovakr.lesson6;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextGroup, editTextNumber, editTextFavoriteMovie;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextGroup = findViewById(R.id.editTextGroup);
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextFavoriteMovie = findViewById(R.id.editTextFavoriteMovie);
        Button buttonSave = findViewById(R.id.buttonSave);

        sharedPref = getSharedPreferences("mirea_settings", Context.MODE_PRIVATE);

        loadSavedData();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void saveData() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("GROUP", editTextGroup.getText().toString());
        editor.putInt("NUMBER", Integer.parseInt(editTextNumber.getText().toString().isEmpty() ? "0" : editTextNumber.getText().toString()));
        editor.putString("FAVORITE_MOVIE", editTextFavoriteMovie.getText().toString());
        editor.apply();
    }

    private void loadSavedData() {
        String group = sharedPref.getString("GROUP", "");
        int number = sharedPref.getInt("NUMBER", 0);
        String favoriteMovie = sharedPref.getString("FAVORITE_MOVIE", "");

        editTextGroup.setText(group);
        editTextNumber.setText(String.valueOf(number));
        editTextFavoriteMovie.setText(favoriteMovie);
    }
}