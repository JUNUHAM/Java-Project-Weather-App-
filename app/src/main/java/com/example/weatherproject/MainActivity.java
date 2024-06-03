package com.example.weatherproject;

import com.example.api.*;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;


public class MainActivity extends AppCompatActivity {

    private NowWeather nowWeather;
    private TodayWeather todayWeather;
    private WeekTem12 weektem12;
    private WeekTem37 weektem37;
    private WeekP12 weekp12;
    private WeekP37 weekp37;

    TextView cityName;
    private ExecutorService executorService;
    public int selectedPosition = 0;

     private int nx=60;
     private int ny=126;
     private String regId="11B10101";

    String[] itmes = {"서울", "인천", "대전", "대구", "울산", "부산", "광주", "안양"};
    private void fetchWeatherDataAndUpdateUI() {
        ExecutorService executor = Executors.newFixedThreadPool(6); // Create a thread pool with 6 threads

        Callable<String> nowWeatherTask = nowWeather::fetchWeatherData;
        Callable<String> todayWeatherTask = todayWeather::fetchWeatherData;
        Callable<String> weekWeather12Task = weektem12::fetchWeatherData;
        Callable<String> weekWeather37Task = weektem37::fetchWeatherData;
        Callable<String> weekPercent12Task = weekp12::fetchWeatherData;
        Callable<String> weekPercent37Task = weekp37::fetchWeatherData;

        List<Callable<String>> taskList = Arrays.asList(
                nowWeatherTask,
                todayWeatherTask,
                weekWeather12Task,
                weekWeather37Task,
                weekPercent12Task,
                weekPercent37Task
        );

        new Thread(() -> {
            try {
                List<Future<String>> futures = executor.invokeAll(taskList);

                // Extract results from futures
                String nowweatherData = futures.get(0).get();
                String todayweatherData = futures.get(1).get();
                String weekweatherData12 = futures.get(2).get();
                String weekweatherData37 = futures.get(3).get();
                String weekPercentData12 = futures.get(4).get();
                String weekPercentData37 = futures.get(5).get();

                String[] todayweatherDataArray = todayweatherData.split("\n");
                String[] weatherDataArray12 = weekweatherData12.split("\n");
                String[] weatherDataArray37 = weekweatherData37.split("\n");
                String[] PercentDataArray12 = weekPercentData12.split("\n");
                String[] PercentDataArray37 = weekPercentData37.split("\n");

                runOnUiThread(() -> {
                    TextView nowTemTextView = findViewById(R.id.nowTem);
                    nowTemTextView.setText(nowweatherData);

                    for (int i = 0; i < 7; i++) {
                        int textViewId = getResources().getIdentifier("daytem" + (i + 1), "id", getPackageName());
                        TextView dayTemTextView = findViewById(textViewId);

                        if (i < todayweatherDataArray.length) {
                            dayTemTextView.setText(todayweatherDataArray[i]);
                        } else {
                            dayTemTextView.setText("N/A");
                        }
                    }

                    for (int i = 1; i <= 2; i++) {
                        int textViewId = getResources().getIdentifier("weektem" + i, "id", getPackageName());
                        TextView weekTemTextView12 = findViewById(textViewId);
                        weekTemTextView12.setText(weatherDataArray12[i - 1]);
                    }

                    for (int i = 3; i <= 6; i++) {
                        int textViewId = getResources().getIdentifier("weektem" + i, "id", getPackageName());
                        TextView weekTemTextView37 = findViewById(textViewId);
                        weekTemTextView37.setText(weatherDataArray37[i - 3]);
                    }

                    for (int i = 1; i <= 2; i++) {
                        int textViewId = getResources().getIdentifier("weekp" + i, "id", getPackageName());
                        TextView weekperTextView12 = findViewById(textViewId);
                        weekperTextView12.setText(PercentDataArray12[i - 1]);
                    }

                    for (int i = 3; i <= 6; i++) {
                        int textViewId = getResources().getIdentifier("weekp" + i, "id", getPackageName());
                        TextView weekperTextView37 = findViewById(textViewId);
                        weekperTextView37.setText(PercentDataArray37[i - 3]);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                executor.shutdown(); // Always shutdown the executor
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // 시간 처리
        // 현재 시간 가져오기
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM월 dd일 HH시", Locale.KOREA);
        String currentTime = dateFormat.format(calendar.getTime());

        // Nowtime TextView 식별
        TextView nowTimeTextView = findViewById(R.id.Nowtime);

        // 현재 시간 설정
        nowTimeTextView.setText(currentTime);

        // 시간 처리
        // 현재 시간 가져오기
        Calendar calendar24 = Calendar.getInstance();
        SimpleDateFormat dateFormat24 = new SimpleDateFormat("HH", Locale.KOREA);
        String currentTime24 = dateFormat24.format(calendar24.getTime());

        // time241부터 time247까지의 TextView를 반복문으로 설정
        for (int i = 241; i <= 247; i++) {
            // TextView 식별
            int textViewId = getResources().getIdentifier("time" + i, "id", getPackageName());
            TextView timeTextView = findViewById(textViewId);

            // 현재 시간 설정
            timeTextView.setText(currentTime24 + "시");

            // 다음 시간 계산 (현재 시간으로부터 1시간씩 증가)
            calendar24.add(Calendar.HOUR_OF_DAY, 1);
            currentTime24 = dateFormat24.format(calendar24.getTime());
        }
        // 일 처리
        // 다음날부터 일자 가져오기
        Calendar calendar7 = Calendar.getInstance();
        calendar7.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat dateFormat7 = new SimpleDateFormat("dd", Locale.KOREA);
        String currentTime7 = dateFormat7.format(calendar7.getTime());
        for (int i = 72; i <= 77; i++) {
            // TextView 식별
            int textViewId = getResources().getIdentifier("time" + i, "id", getPackageName());
            TextView timeTextView = findViewById(textViewId);

            // 현재 시간 설정
            timeTextView.setText(currentTime7 + "일");

            // 다음 날짜 계산 (현재 날짜로부터 1일씩 증가)
            calendar7.add(Calendar.DAY_OF_MONTH, 1);
            currentTime7 = dateFormat7.format(calendar7.getTime());
        }

        cityName = findViewById(R.id.cityName);
        Spinner spinner = findViewById(R.id.my_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itmes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityName.setText(itmes[position]);
                TodayNow today = new TodayNow();
                exToday extoday = new exToday();
                Hour514 time5 = new Hour514();
                Hour618 time6 = new Hour618();

                switch (position) {
                    case 0: //서울
                        nx = 60;
                        ny = 126;
                        regId = "11B10101";
                        nowWeather = new NowWeather(today.formattedDate1, today.formattedDate2, nx, ny);
                        todayWeather = new TodayWeather( extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weektem12 = new WeekTem12(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weektem37 = new WeekTem37(extoday.getFormattedDate(), time6.getFormattedTime(), regId);
                        weekp12 = new WeekP12(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weekp37 = new WeekP37(extoday.getFormattedDate(), time6.getFormattedTime(), regId);
                        fetchWeatherDataAndUpdateUI();
                        break;
                    case 1: //인천
                        nx = 56;
                        ny = 126;
                        regId = "11B20201";
                        nowWeather = new NowWeather(today.formattedDate1, today.formattedDate2, nx, ny);
                        todayWeather = new TodayWeather( extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weektem12 = new WeekTem12(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weektem37 = new WeekTem37(extoday.getFormattedDate(), time6.getFormattedTime(), regId);
                        weekp12 = new WeekP12(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weekp37 = new WeekP37(extoday.getFormattedDate(), time6.getFormattedTime(), regId);
                        fetchWeatherDataAndUpdateUI();
                        break;
                    case 2: //대전
                        nx = 67;
                        ny = 100;
                        regId = "11C20401";
                        nowWeather = new NowWeather(today.formattedDate1, today.formattedDate2, nx, ny);
                        todayWeather = new TodayWeather( extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weektem12 = new WeekTem12(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weektem37 = new WeekTem37(extoday.getFormattedDate(), time6.getFormattedTime(), regId);
                        weekp12 = new WeekP12(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weekp37 = new WeekP37(extoday.getFormattedDate(), time6.getFormattedTime(), regId);
                        fetchWeatherDataAndUpdateUI();
                        break;
                    case 3: //대구
                        nx = 89;
                        ny = 90;
                        regId = "11H10701";
                        nowWeather = new NowWeather(today.formattedDate1, today.formattedDate2, nx, ny);
                        todayWeather = new TodayWeather( extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weektem12 = new WeekTem12(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weektem37 = new WeekTem37(extoday.getFormattedDate(), time6.getFormattedTime(), regId);
                        weekp12 = new WeekP12(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weekp37 = new WeekP37(extoday.getFormattedDate(), time6.getFormattedTime(), regId);
                        fetchWeatherDataAndUpdateUI();
                        break;
                    case 4: //울산
                        nx = 102;
                        ny = 84;
                        regId = "11H20101";
                        nowWeather = new NowWeather(today.formattedDate1, today.formattedDate2, nx, ny);
                        todayWeather = new TodayWeather( extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weektem12 = new WeekTem12(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weektem37 = new WeekTem37(extoday.getFormattedDate(), time6.getFormattedTime(), regId);
                        weekp12 = new WeekP12(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weekp37 = new WeekP37(extoday.getFormattedDate(), time6.getFormattedTime(), regId);
                        fetchWeatherDataAndUpdateUI();
                        break;
                    case 5: //부산
                        nx = 98;
                        ny = 76;
                        regId = "11H20201";
                        nowWeather = new NowWeather(today.formattedDate1, today.formattedDate2, nx, ny);
                        todayWeather = new TodayWeather( extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weektem12 = new WeekTem12(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weektem37 = new WeekTem37(extoday.getFormattedDate(), time6.getFormattedTime(), regId);
                        weekp12 = new WeekP12(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weekp37 = new WeekP37(extoday.getFormattedDate(), time6.getFormattedTime(), regId);
                        fetchWeatherDataAndUpdateUI();
                        break;
                    case 6: //광주
                        nx = 58;
                        ny = 74;
                        regId = "11B20702";
                        nowWeather = new NowWeather(today.formattedDate1, today.formattedDate2, nx, ny);
                        todayWeather = new TodayWeather( extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weektem12 = new WeekTem12(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weektem37 = new WeekTem37(extoday.getFormattedDate(), time6.getFormattedTime(), regId);
                        weekp12 = new WeekP12(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weekp37 = new WeekP37(extoday.getFormattedDate(), time6.getFormattedTime(), regId);
                        fetchWeatherDataAndUpdateUI();
                        break;
                    case 7: //안양
                        nx = 59;
                        ny = 123;
                        regId = "11B20602";
                        nowWeather = new NowWeather(today.formattedDate1, today.formattedDate2, nx, ny);
                        todayWeather = new TodayWeather( extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weektem12 = new WeekTem12(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weektem37 = new WeekTem37(extoday.getFormattedDate(), time6.getFormattedTime(), regId);
                        weekp12 = new WeekP12(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
                        weekp37 = new WeekP37(extoday.getFormattedDate(), time6.getFormattedTime(), regId);
                        fetchWeatherDataAndUpdateUI();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cityName.setText("도시 선택");
            }
        });
    }
}