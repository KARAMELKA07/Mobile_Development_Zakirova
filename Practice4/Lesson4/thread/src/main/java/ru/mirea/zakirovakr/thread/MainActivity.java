package ru.mirea.zakirovakr.thread;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import ru.mirea.zakirovakr.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Thread mainThread = Thread.currentThread();
        binding.textViewResult.setText("Имя текущего потока: " + mainThread.getName());
        mainThread.setName("МОЙ НОМЕР ГРУППЫ: БСБО-09-22, НОМЕР ПО СПИСКУ: 10");
        binding.textViewResult.append("\nНовое имя потока: " + mainThread.getName());
        Log.d(MainActivity.class.getSimpleName(), "Stack: " + java.util.Arrays.toString(mainThread.getStackTrace()));

        binding.buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int threadNumber = counter++;
                        Log.d("ThreadProject", String.format("Запущен поток № %d студентом группы № %s номер по списку № %d",
                                threadNumber, "БСБО-09-22", 10));

                        String totalClassesStr = binding.editTextTotalClasses.getText().toString();
                        String studyDaysStr = binding.editTextStudyDays.getText().toString();

                        try {
                            int totalClasses = Integer.parseInt(totalClassesStr);
                            int studyDays = Integer.parseInt(studyDaysStr);

                            float averageClassesPerDay = (float) totalClasses / studyDays;

                            runOnUiThread(() -> {
                                binding.textViewResult.setText("Среднее количество пар в день: " + averageClassesPerDay);
                            });

                            long endTime = System.currentTimeMillis() + 20 * 1000;
                            while (System.currentTimeMillis() < endTime) {
                                synchronized (this) {
                                    try {
                                        wait(endTime - System.currentTimeMillis());
                                        Log.d(MainActivity.class.getSimpleName(), "Endtime: " + endTime);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                            Log.d("ThreadProject", "Выполнен поток № " + threadNumber);
                        } catch (NumberFormatException e) {
                            runOnUiThread(() -> {
                                binding.textViewResult.setText("Ошибка: введите корректные числа");
                            });
                        } catch (ArithmeticException e) {
                            runOnUiThread(() -> {
                                binding.textViewResult.setText("Ошибка: количество дней не может быть 0");
                            });
                        }
                    }
                }).start();
            }
        });
    }
}