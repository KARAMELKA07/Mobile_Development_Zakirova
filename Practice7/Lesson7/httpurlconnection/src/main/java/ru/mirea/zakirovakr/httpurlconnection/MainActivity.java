package ru.mirea.zakirovakr.httpurlconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import ru.mirea.zakirovakr.httpurlconnection.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = null;
                if (connectivityManager != null) {
                    networkInfo = connectivityManager.getActiveNetworkInfo();
                }
                if (networkInfo != null && networkInfo.isConnected()) {
                    new DownloadPageTask().execute("https://ipinfo.io/json");
                } else {
                    Toast.makeText(MainActivity.this, "Нет интернета", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class DownloadPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.textViewIp.setText("Загружаем...");
            binding.textViewCity.setText("Загружаем...");
            binding.textViewRegion.setText("Загружаем...");
            binding.textViewCountry.setText("Загружаем...");
            binding.textViewWeather.setText("Загружаем...");
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                String ipInfo = downloadIpInfo(urls[0]);
                JSONObject ipJson = new JSONObject(ipInfo);
                String latitude = ipJson.getString("loc").split(",")[0];
                String longitude = ipJson.getString("loc").split(",")[1];
                String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                        "&longitude=" + longitude + "&current_weather=true";
                String weatherInfo = downloadIpInfo(weatherUrl);
                JSONObject combinedJson = new JSONObject();
                combinedJson.put("ip", ipJson.getString("ip"));
                combinedJson.put("city", ipJson.getString("city"));
                combinedJson.put("region", ipJson.getString("region"));
                combinedJson.put("country", ipJson.getString("country"));
                combinedJson.put("weather", new JSONObject(weatherInfo).getJSONObject("current_weather").getString("temperature"));
                return combinedJson.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject responseJson = new JSONObject(result);
                binding.textViewIp.setText("IP: " + responseJson.getString("ip"));
                binding.textViewCity.setText("City: " + responseJson.getString("city"));
                binding.textViewRegion.setText("Region: " + responseJson.getString("region"));
                binding.textViewCountry.setText("Country: " + responseJson.getString("country"));
                binding.textViewWeather.setText("Weather: " + responseJson.getString("weather") + "°C");
                Log.d(TAG, "Response: " + responseJson);
            } catch (Exception e) {
                e.printStackTrace();
                binding.textViewIp.setText("Error: " + result);
                binding.textViewCity.setText("");
                binding.textViewRegion.setText("");
                binding.textViewCountry.setText("");
                binding.textViewWeather.setText("");
            }
        }
    }

    private String downloadIpInfo(String address) throws IOException {
        InputStream inputStream = null;
        String data = "";
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(100000);
            connection.setConnectTimeout(100000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read;
                while ((read = inputStream.read()) != -1) {
                    bos.write(read);
                }
                bos.close();
                data = bos.toString();
            } else {
                data = connection.getResponseMessage() + ". Error Code: " + responseCode;
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return data;
    }
}