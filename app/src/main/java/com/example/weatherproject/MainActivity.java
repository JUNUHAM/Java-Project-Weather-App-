package com.example.weatherproject;

import com.example.api.*;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    TextView cityName;
    private ExecutorService executorService;

    String[] itmes = {"서울", "인천", "대전", "대구", "울산", "부산", "광주", "안양"};
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

        cityName = findViewById(R.id.cityName);
        Spinner spinner = findViewById(R.id.my_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itmes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityName.setText(itmes[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cityName.setText("도시 선택");
            }
        });
        // time241부터 time247까지의 TextView를 반복문으로 설정
        for (int i = 241; i <= 247; i++) {
            // TextView 식별
            int textViewId = getResources().getIdentifier("time" + i, "id", getPackageName());
            TextView timeTextView = findViewById(textViewId);

            // 현재 시간 설정
            timeTextView.setText(currentTime24+"시");

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
            timeTextView.setText(currentTime7+"일");

            // 다음 날짜 계산 (현재 날짜로부터 1일씩 증가)
            calendar7.add(Calendar.DAY_OF_MONTH, 1);
            currentTime7 = dateFormat7.format(calendar7.getTime());
        }

        TodayNow today = new TodayNow();
        exToday extoday=new exToday();
        Hour514 time5 = new Hour514();
        Hour618 time6 = new Hour618();

        NowWeather nowWeather = new NowWeather("1jdnhESiJyvL8T7ZVy%2FIF%2BLijO8GdJmzjAJptRzoWNgn%2FVAXr%2BP79CxEmEoEGkq1MqFTzFgOjnQWICts87VfmQ%3D%3D",today.formattedDate1,today.formattedDate2,59,125);
        TodayWeather todayWeather = new TodayWeather("1jdnhESiJyvL8T7ZVy%2FIF%2BLijO8GdJmzjAJptRzoWNgn%2FVAXr%2BP79CxEmEoEGkq1MqFTzFgOjnQWICts87VfmQ%3D%3D", extoday.getFormattedDate(), time5.getFormattedTime(), 59, 125);
        WeekTem12 weektem12 = new WeekTem12("1jdnhESiJyvL8T7ZVy%2FIF%2BLijO8GdJmzjAJptRzoWNgn%2FVAXr%2BP79CxEmEoEGkq1MqFTzFgOjnQWICts87VfmQ%3D%3D", extoday.getFormattedDate(),time5.getFormattedTime(), 59, 125);
        WeekTem37 weektem37 = new WeekTem37("1jdnhESiJyvL8T7ZVy%2FIF%2BLijO8GdJmzjAJptRzoWNgn%2FVAXr%2BP79CxEmEoEGkq1MqFTzFgOjnQWICts87VfmQ%3D%3D", extoday.getFormattedDate(), time6.getFormattedTime());
        WeekP12 weekp12 =new WeekP12("1jdnhESiJyvL8T7ZVy%2FIF%2BLijO8GdJmzjAJptRzoWNgn%2FVAXr%2BP79CxEmEoEGkq1MqFTzFgOjnQWICts87VfmQ%3D%3D", extoday.getFormattedDate(), time5.getFormattedTime(), 59, 125);
        WeekP37 weekp37 =new WeekP37("1jdnhESiJyvL8T7ZVy%2FIF%2BLijO8GdJmzjAJptRzoWNgn%2FVAXr%2BP79CxEmEoEGkq1MqFTzFgOjnQWICts87VfmQ%3D%3D", extoday.getFormattedDate(), time6.getFormattedTime());

        executorService = Executors.newSingleThreadExecutor();

        executorService.execute(() -> {
            try {
                String nowweatherData = nowWeather.fetchWeatherData();

                String todayweatherData = todayWeather.fetchWeatherData();
                String[] todayweatherDataArray = todayweatherData.split("\n");

                String weekweatherData12 = weektem12.fetchWeatherData();
                String[] weatherDataArray12 = weekweatherData12.split("\n");

                String weekweatherData37 = weektem37.fetchWeatherData();
                String[] weatherDataArray37 = weekweatherData37.split("\n");

                String weekPercentData12 = weekp12.fetchWeatherData();
                String[] PercentDataArray12 = weekPercentData12.split("\n");

                String weekPercentData37 = weekp37.fetchWeatherData();
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
                            dayTemTextView.setText("N/A"); // 데이터가 부족한 경우 "N/A"로 표시
                        }
                    }

                    for (int i = 1; i <= 2; i++) {
                        int textViewId = getResources().getIdentifier("weektem" + i, "id", getPackageName());
                        TextView weekTemTextView12 = findViewById(textViewId);
                        weekTemTextView12.setText(weatherDataArray12[i - 1]); // i - 1을 사용하여 배열 인덱스와 TextView의 인덱스를 맞춤
                    }

                    for (int i = 3; i <= 6; i++) {
                        int textViewId = getResources().getIdentifier("weektem" + i, "id", getPackageName());
                        TextView weekTemTextView37 = findViewById(textViewId);
                        weekTemTextView37.setText(weatherDataArray37[i - 3]); // i - 3을 사용하여 배열 인덱스와 TextView의 인덱스를 맞춤
                    }

                    for (int i = 1; i <= 2; i++) {
                        int textViewId = getResources().getIdentifier("weekp" + i, "id", getPackageName());
                        TextView weekperTextView12 = findViewById(textViewId);
                        weekperTextView12.setText(PercentDataArray12[i - 1]); // i - 1을 사용하여 배열 인덱스와 TextView의 인덱스를 맞춤
                    }

                    for (int i = 3; i <= 6; i++) {
                        int textViewId = getResources().getIdentifier("weekp" + i, "id", getPackageName());
                        TextView weekperTextView37 = findViewById(textViewId);
                        weekperTextView37.setText(PercentDataArray37[i - 3]); // i - 3을 사용하여 배열 인덱스와 TextView의 인덱스를 맞춤
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 액티비티가 종료될 때 ExecutorService를 종료
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}