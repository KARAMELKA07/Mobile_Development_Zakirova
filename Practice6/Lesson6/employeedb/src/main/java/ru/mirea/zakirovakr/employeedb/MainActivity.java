package ru.mirea.zakirovakr.employeedb;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText editTextName, editTextSuperpower, editTextPowerLevel;
    private TextView textViewHeroes;
    private SuperheroDao superheroDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        editTextSuperpower = findViewById(R.id.editTextSuperpower);
        editTextPowerLevel = findViewById(R.id.editTextPowerLevel);
        textViewHeroes = findViewById(R.id.textViewHeroes);
        Button buttonSave = findViewById(R.id.buttonSave);
        Button buttonLoad = findViewById(R.id.buttonLoad);
        Button buttonDelete = findViewById(R.id.buttonDelete);

        AppDatabase db = App.getInstance().getDatabase();
        superheroDao = db.superheroDao();

        addInitialHeroes();

        updateHeroesList();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSuperhero();
            }
        });

        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSuperhero();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSuperhero();
            }
        });
    }

    private void addInitialHeroes() {
        Superhero hero1 = new Superhero();
        hero1.name = "Starlight";
        hero1.superpower = "Light Manipulation";
        hero1.powerLevel = 85;
        superheroDao.insert(hero1);

        Superhero hero2 = new Superhero();
        hero2.name = "Thunderbolt";
        hero2.superpower = "Electricity Control";
        hero2.powerLevel = 90;
        superheroDao.insert(hero2);
    }

    private void saveSuperhero() {
        String name = editTextName.getText().toString().trim();
        String superpower = editTextSuperpower.getText().toString().trim();
        String powerLevelStr = editTextPowerLevel.getText().toString().trim();

        if (name.isEmpty() || superpower.isEmpty() || powerLevelStr.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int powerLevel = Integer.parseInt(powerLevelStr);
            Superhero superhero = new Superhero();
            superhero.name = name;
            superhero.superpower = superpower;
            superhero.powerLevel = powerLevel;
            superheroDao.insert(superhero);
            Toast.makeText(this, "Супергерой сохранён", Toast.LENGTH_SHORT).show();
            updateHeroesList();
            clearFields();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Уровень силы должен быть числом", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSuperhero() {
        String name = editTextName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Введите имя для поиска", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Superhero> heroes = superheroDao.getAll();
        for (Superhero hero : heroes) {
            if (hero.name.equalsIgnoreCase(name)) {
                editTextName.setText(hero.name);
                editTextSuperpower.setText(hero.superpower);
                editTextPowerLevel.setText(String.valueOf(hero.powerLevel));
                Toast.makeText(this, "Супергерой загружен", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(this, "Супергерой не найден", Toast.LENGTH_SHORT).show();
    }

    private void deleteSuperhero() {
        String name = editTextName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Введите имя для удаления", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Superhero> heroes = superheroDao.getAll();
        for (Superhero hero : heroes) {
            if (hero.name.equalsIgnoreCase(name)) {
                superheroDao.delete(hero);
                Toast.makeText(this, "Супергерой удалён", Toast.LENGTH_SHORT).show();
                updateHeroesList();
                clearFields();
                return;
            }
        }
        Toast.makeText(this, "Супергерой не найден", Toast.LENGTH_SHORT).show();
    }

    private void updateHeroesList() {
        List<Superhero> heroes = superheroDao.getAll();
        StringBuilder sb = new StringBuilder("Список супергероев:\n");
        for (Superhero hero : heroes) {
            sb.append("ID: ").append(hero.id)
                    .append(", Имя: ").append(hero.name)
                    .append(", Суперсила: ").append(hero.superpower)
                    .append(", Уровень: ").append(hero.powerLevel)
                    .append("\n");
        }
        textViewHeroes.setText(sb.toString());
        Log.d(TAG, sb.toString());
    }

    private void clearFields() {
        editTextName.setText("");
        editTextSuperpower.setText("");
        editTextPowerLevel.setText("");
    }
}