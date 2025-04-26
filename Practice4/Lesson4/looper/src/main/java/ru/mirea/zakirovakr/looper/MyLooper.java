package ru.mirea.zakirovakr.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyLooper extends Thread {
    public Handler mHandler;
    private Handler mainHandler;

    public MyLooper(Handler mainThreadHandler) {
        mainHandler = mainThreadHandler;
    }

    public void run() {
        Log.d("MyLooper", "run");
        Looper.prepare();

        mHandler = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                String ageStr = msg.getData().getString("AGE");
                String profession = msg.getData().getString("PROFESSION");

                try {
                    int age = Integer.parseInt(ageStr);
                    // Задержка на количество лет (в секундах)
                    Thread.sleep(age * 1000);

                    // Формирование результата
                    String result = String.format("Возраст: %d, Профессия: %s", age, profession);
                    // Логирование времени обработки
                    String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    Log.d("MyLooper", "Processed at " + time + ". Результат: " + result);

                    // Отправка результата обратно в главный поток
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("result", result);
                    message.setData(bundle);
                    mainHandler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (NumberFormatException e) {
                    Log.e("MyLooper", "Ошибка: возраст должен быть числом");
                }
            }
        };

        Looper.loop();
    }
}