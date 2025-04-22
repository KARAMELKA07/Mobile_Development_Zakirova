package ru.mirea.zakirovakr.simplefragmentapp2;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {
    private static final String CURRENT_FRAGMENT = "current_fragment";
    private boolean isLandscape;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Определяем ориентацию
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        setContentView(R.layout.activity_main);

        // Восстанавливаем фрагмент если есть сохраненное состояние
        if (savedInstanceState != null) {
            currentFragment = getSupportFragmentManager().getFragment(savedInstanceState, CURRENT_FRAGMENT);
        }

        // Инициализация только при первом создании
        if (savedInstanceState == null) {
            if (isLandscape) {
                // В ландшафте оба фрагмента уже в XML
                return;
            } else {
                // В портрете устанавливаем первый фрагмент
                currentFragment = new FirstFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, currentFragment)
                        .commit();
            }
        }

        // Настройка кнопок только для портретной ориентации
        if (!isLandscape) {
            Button btnFirst = findViewById(R.id.btnFirstFragment);
            Button btnSecond = findViewById(R.id.btnSecondFragment);

            btnFirst.setOnClickListener(v -> switchFragment(new FirstFragment()));
            btnSecond.setOnClickListener(v -> switchFragment(new SecondFragment()));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Сохраняем текущий фрагмент
        if (currentFragment != null && currentFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, CURRENT_FRAGMENT, currentFragment);
        }
    }

    private void switchFragment(Fragment fragment) {
        currentFragment = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .addToBackStack(null)
                .commit();
    }
}