package ru.mirea.zakirovakr.data_thread;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import ru.mirea.zakirovakr.data_thread.databinding.ActivityMainBinding;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Инициализация ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Настройка WindowInsets
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Определение Runnable
        final Runnable runn1 = new Runnable() {
            public void run() {
                binding.tvInfo.setText("runn1\n" + binding.tvInfo.getText());
            }
        };

        final Runnable runn2 = new Runnable() {
            public void run() {
                binding.tvInfo.setText("runn2\n" + binding.tvInfo.getText());
            }
        };

        final Runnable runn3 = new Runnable() {
            public void run() {
                binding.tvInfo.setText("runn3\n" + binding.tvInfo.getText());
                // После выполнения всех Runnable добавляем описание методов
                binding.tvInfo.append("\nРазличия и последовательность:\n" +
                        "1. runOnUiThread: Выполняет задачу в UI-потоке немедленно.\n" +
                        "2. post: Также выполняет задачу в UI-потоке немедленно, но через View.\n" +
                        "3. postDelayed: Выполняет задачу с задержкой (в данном случае 2000 мс).\n" +
                        "Последовательность: runn1 -> runn2 -> runn3 (с задержкой).");
            }
        };

        // Запуск фонового потока
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    // Задержка 2 секунды
                    TimeUnit.SECONDS.sleep(2);
                    runOnUiThread(runn1); // Выполняется сразу после 2 сек

                    // Задержка 1 секунда
                    TimeUnit.SECONDS.sleep(1);
                    binding.tvInfo.postDelayed(runn3, 2000); // Выполнится через 2 сек после вызова
                    binding.tvInfo.post(runn2); // Выполняется сразу после предыдущей задержки
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}