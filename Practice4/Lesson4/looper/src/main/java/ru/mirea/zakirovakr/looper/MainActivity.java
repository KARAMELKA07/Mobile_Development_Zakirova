package ru.mirea.zakirovakr.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import ru.mirea.zakirovakr.looper.databinding.ActivityMainBinding;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MyLooper myLooper;

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

        // Создание Handler для главного потока
        Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String result = msg.getData().getString("result");
                // Форматирование времени получения результата
                String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                Log.d(MainActivity.class.getSimpleName(), "Task executed at " + time + ". Result: " + result);
            }
        };

        // Запуск MyLooper
        myLooper = new MyLooper(mainThreadHandler);
        myLooper.start();

        // Установка начальных значений
        binding.editTextAge.setText("25");
        binding.editTextProfession.setText("Программист");

        // Обработчик нажатия на кнопку
        binding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Логирование времени клика
                String clickTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                Log.d(MainActivity.class.getSimpleName(), "Button clicked at " + clickTime);

                // Проверка, что Handler в MyLooper инициализирован
                if (myLooper.mHandler != null) {
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString("AGE", binding.editTextAge.getText().toString());
                    bundle.putString("PROFESSION", binding.editTextProfession.getText().toString());
                    msg.setData(bundle);
                    myLooper.mHandler.sendMessage(msg);
                } else {
                    Log.e(MainActivity.class.getSimpleName(), "Handler в MyLooper ещё не инициализирован");
                }
            }
        });
    }
}