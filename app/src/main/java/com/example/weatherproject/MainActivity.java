package com.example.weatherproject;

import com.example.api1.*;
import com.example.api2.NowWeatherImage;
import com.example.api2.TodayWeatherImage;
import com.example.api2.Week1Image;
import com.example.api2.Week2Image;
import com.example.api2.Week37Image;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {

    //api1
    private NowWeatherText nowWeathertext;
    private TodayWeatherText todayWeathertext;
    private WeekTem12Text weektem12text;
    private WeekTem37Text weektem37text;
    private WeekP12Text weekp12text;
    private WeekP37Text weekp37text;
    //api2
    private NowWeatherImage nowweatherimage;
    private TodayWeatherImage todayweatherimage;
    private Week1Image week1image;
    private Week2Image week2image;
    private Week37Image week37image;
    TextView cityName;
     //지역 초기화용
     private int nx=60;
     private int ny=126;
     private String regId1="11B10101";
     private String regId2="11B10101";

    private void api1(int nx, int ny, String regId1,String regId2){
        TodayNow today = new TodayNow();
        exToday extoday = new exToday();
        Hour514 time5 = new Hour514();
        Hour618 time6 = new Hour618();
        nowWeathertext = new NowWeatherText(today.formattedDate1, today.formattedDate2, nx, ny);
        todayWeathertext = new TodayWeatherText( extoday.getFormattedDate(), time5.getFormattedTime(),nx,ny);
        weektem12text = new WeekTem12Text(extoday.getFormattedDate(), time5.getFormattedTime(),nx,ny);
        weektem37text = new WeekTem37Text(extoday.getFormattedDate(), time6.getFormattedTime(),regId1);
        weekp12text = new WeekP12Text(extoday.getFormattedDate(), time5.getFormattedTime(),nx,ny);
        weekp37text = new WeekP37Text(extoday.getFormattedDate(), time6.getFormattedTime(),regId2);
    }
    private void api2(int nx ,int ny ,String regId2){
        exToday extoday = new exToday();
        Hour514 time5 = new Hour514();
        Hour618 time6 = new Hour618();
        nowweatherimage =new NowWeatherImage(extoday.getFormattedDate() , time5.getFormattedTime(),nx,ny);
        todayweatherimage =new TodayWeatherImage(extoday.getFormattedDate() , time5.getFormattedTime(),nx,ny);
        week1image = new Week1Image(extoday.getFormattedDate(),time5.getFormattedTime(),nx,ny);
        week2image = new Week2Image(extoday.getFormattedDate(),time5.getFormattedTime(),nx,ny);
        week37image =new Week37Image(extoday.getFormattedDate(), time6.getFormattedTime(),regId2);
    }
    String[] itmes = {"서울", "인천", "대전", "대구", "울산", "부산", "광주", "안양"};
    private void fetchWeatherDataAndUpdateTextUI() {
        ExecutorService executor = Executors.newFixedThreadPool(6); // Create a thread pool with 6 threads

        Callable<String> nowWeathertextTask = nowWeathertext::fetchWeatherData;
        Callable<String> todayWeathertextTask = todayWeathertext::fetchWeatherData;
        Callable<String> weekWeather12textTask = weektem12text::fetchWeatherData;
        Callable<String> weekWeather37textTask = weektem37text::fetchWeatherData;
        Callable<String> weekPercent12textTask = weekp12text::fetchWeatherData;
        Callable<String> weekPercent37textTask = weekp37text::fetchWeatherData;

        List<Callable<String>> taskList = Arrays.asList(
                nowWeathertextTask,
                todayWeathertextTask,
                weekWeather12textTask,
                weekWeather37textTask,
                weekPercent12textTask,
                weekPercent37textTask
        );

        new Thread(() -> {
            try {
                List<Future<String>> futures = executor.invokeAll(taskList);

                // Extract results from futures
                String nowweathertextData = futures.get(0).get();
                String todayweathertextData = futures.get(1).get();
                String weekweathertextData12 = futures.get(2).get();
                String weekweathertextData37 = futures.get(3).get();
                String weekPercenttextData12 = futures.get(4).get();
                String weekPercenttextData37 = futures.get(5).get();

                String[] todayweatherDataArray = todayweathertextData.split("\n");
                String[] weatherDataArray12 = weekweathertextData12.split("\n");
                String[] weatherDataArray37 = weekweathertextData37.split("\n");
                String[] PercentDataArray12 = weekPercenttextData12.split("\n");
                String[] PercentDataArray37 = weekPercenttextData37.split("\n");

                runOnUiThread(() -> {
                    TextView nowTemTextView = findViewById(R.id.nowTem);
                    nowTemTextView.setText(nowweathertextData);

                    for (int i = 0; i < 7; i++) {
                        @SuppressLint("DiscouragedApi") int textViewId = getResources().getIdentifier("daytem" + (i + 1), "id", getPackageName());
                        TextView dayTemTextView = findViewById(textViewId);

                        if (i < todayweatherDataArray.length) {
                            dayTemTextView.setText(todayweatherDataArray[i]);
                        } else {
                            dayTemTextView.setText("N/A");
                        }
                    }

                    for (int i = 0; i <= 1; i++) {
                        @SuppressLint("DiscouragedApi") int textViewId = getResources().getIdentifier("weektem" + (i+1), "id", getPackageName());
                        TextView weekTemTextView12 = findViewById(textViewId);
                        weekTemTextView12.setText(weatherDataArray12[i]);
                    }

                    for (int i = 0; i <= 3; i++) {
                        @SuppressLint("DiscouragedApi") int textViewId = getResources().getIdentifier("weektem" + (i+3), "id", getPackageName());
                        TextView weekTemTextView37 = findViewById(textViewId);
                        weekTemTextView37.setText(weatherDataArray37[i]);
                    }

                    for (int i = 0; i <= 1; i++) {
                        @SuppressLint("DiscouragedApi") int textViewId = getResources().getIdentifier("weekp" + (i+1), "id", getPackageName());
                        TextView weekperTextView12 = findViewById(textViewId);
                        weekperTextView12.setText(PercentDataArray12[i]);
                    }

                    for (int i = 0; i <= 3; i++) {
                        @SuppressLint("DiscouragedApi") int textViewId = getResources().getIdentifier("weekp" + (i+3), "id", getPackageName());
                        TextView weekperTextView37 = findViewById(textViewId);
                        weekperTextView37.setText(PercentDataArray37[i]);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                executor.shutdown(); // Always shutdown the executor
            }
        }).start();
    }

    private void fetchWeatherDataAndUpdateImageUI() {
        CompletableFuture<Integer> nowWeatherFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return nowweatherimage.fetchWeatherData();
            } catch (Exception e) {
                e.printStackTrace();
                return 0; // 기본값
            }
        });

        CompletableFuture<Integer> todayWeatherFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return todayweatherimage.fetchWeatherData();
            } catch (Exception e) {
                e.printStackTrace();
                return 0; // 기본값
            }
        });

        CompletableFuture<Integer> tomorrowWeatherFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return week1image.fetchWeatherData();
            } catch (Exception e) {
                e.printStackTrace();
                return 0; // 기본값
            }
        });

        CompletableFuture<Integer> day3WeatherFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return week2image.fetchWeatherData();
            } catch (Exception e) {
                e.printStackTrace();
                return 0; // 기본값
            }
        });

        CompletableFuture<List<Integer>> otherDayWeatherFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return week37image.fetchWeatherData();
            } catch (Exception e) {
                e.printStackTrace();
                return Collections.singletonList(0); // 기본값
            }
        });

        CompletableFuture<Void> allOf = CompletableFuture.allOf(
                nowWeatherFuture,
                todayWeatherFuture,
                tomorrowWeatherFuture,
                day3WeatherFuture,
                otherDayWeatherFuture
        );

        allOf.thenRunAsync(() -> {
            try {
                int nowWeatherValue = nowWeatherFuture.get();
                int todayWeatherValue = todayWeatherFuture.get();
                int tomorrowValue = tomorrowWeatherFuture.get();
                int day3value = day3WeatherFuture.get();
                List<Integer> otherdayvalue = otherDayWeatherFuture.get();

                runOnUiThread(() -> updateImages(nowWeatherValue, todayWeatherValue, tomorrowValue, day3value, otherdayvalue));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void updateImages(int nowWeatherValue, int todayWeatherValue, int tomorrowValue, int day3Value, List<Integer> otherdayValue) {
        ImageView nowTemiconImageView = findViewById(R.id.NowWeatherIcon);
        ImageView nowTembackgroundImageView = findViewById(R.id.nowTemBackGround);

        // weatherValue 값에 따라 이미지 설정
        switch (nowWeatherValue) {
            case 1:
                nowTemiconImageView.setImageResource(R.drawable.sunny_icon);
                nowTembackgroundImageView.setImageResource(R.drawable.sunny);// 맑음
                break;
            case 2:
                nowTemiconImageView.setImageResource(R.drawable.cloud_icon);
                nowTembackgroundImageView.setImageResource(R.drawable.cloud);// 구름 많음
                break;
            case 3:
                nowTemiconImageView.setImageResource(R.drawable.cloudy_icon);
                nowTembackgroundImageView.setImageResource(R.drawable.cloudy);// 흐림
                break;
            case 4:
                nowTemiconImageView.setImageResource(R.drawable.rainy_icon);
                nowTembackgroundImageView.setImageResource(R.drawable.rain);// 비
                break;
            case 5:
                nowTemiconImageView.setImageResource(R.drawable.snowfall_icon);
                nowTembackgroundImageView.setImageResource(R.drawable.snow);// 눈
                break;
            default:
                nowTemiconImageView.setImageResource(R.drawable.loading_logo);
                nowTembackgroundImageView.setImageResource(R.drawable.loading_logo);// 기본 이미지
                break;
        }

        for (int i = 1; i <= 7; i++) {
            @SuppressLint("DiscouragedApi") int ImageViewId = getResources().getIdentifier("dayimage" + i, "id", getPackageName());
            ImageView dayImageView = findViewById(ImageViewId);

            switch (todayWeatherValue) {
                case 1:
                    dayImageView.setImageResource(R.drawable.sunny_icon); // 맑음
                    break;
                case 2:
                    dayImageView.setImageResource(R.drawable.cloud_icon); // 구름 많음
                    break;
                case 3:
                    dayImageView.setImageResource(R.drawable.cloudy_icon); // 흐림
                    break;
                case 4:
                    dayImageView.setImageResource(R.drawable.rainy_icon); // 비
                    break;
                case 5:
                    dayImageView.setImageResource(R.drawable.snowfall_icon); // 눈
                    break;
                default:
                    dayImageView.setImageResource(R.drawable.loading_logo); // 기본 이미지
                    break;
            }
        }

        ImageView day1ImageView = findViewById(R.id.weekimage1);
        switch (tomorrowValue) {
            case 1:
                day1ImageView.setImageResource(R.drawable.sunny_icon); // 맑음
                break;
            case 2:
                day1ImageView.setImageResource(R.drawable.cloud_icon); // 구름 많음
                break;
            case 3:
                day1ImageView.setImageResource(R.drawable.cloudy_icon); // 흐림
                break;
            case 4:
                day1ImageView.setImageResource(R.drawable.rainy_icon); // 비
                break;
            case 5:
                day1ImageView.setImageResource(R.drawable.snowfall_icon); // 눈
                break;
            default:
                day1ImageView.setImageResource(R.drawable.loading_logo); // 기본 이미지
                break;
        }

        ImageView day2ImageView = findViewById(R.id.weekimage2);
        switch (day3Value) {
            case 1:
                day2ImageView.setImageResource(R.drawable.sunny_icon); // 맑음
                break;
            case 2:
                day2ImageView.setImageResource(R.drawable.cloud_icon); // 구름 많음
                break;
            case 3:
                day2ImageView.setImageResource(R.drawable.cloudy_icon); // 흐림
                break;
            case 4:
                day2ImageView.setImageResource(R.drawable.rainy_icon); // 비
                break;
            case 5:
                day2ImageView.setImageResource(R.drawable.snowfall_icon); // 눈
                break;
            default:
                day2ImageView.setImageResource(R.drawable.loading_logo); // 기본 이미지
                break;
        }

        for (int i = 0; i <= 3; i++) {
            int ImageViewId = getResources().getIdentifier("weekimage" + (i + 3), "id", getPackageName());
            ImageView dayImageView = findViewById(ImageViewId);

            int weatherValue = otherdayValue.get(i); // 리스트에서 값을 가져옴

            if (weatherValue == 1) {
                dayImageView.setImageResource(R.drawable.sunny_icon); // 맑음
            } else if (weatherValue == 2) {
                dayImageView.setImageResource(R.drawable.cloud_icon); // 구름 많음
            } else if (weatherValue == 3) {
                dayImageView.setImageResource(R.drawable.cloudy_icon); // 흐림
            } else if (weatherValue == 4) {
                dayImageView.setImageResource(R.drawable.rainy_icon); // 비
            } else if (weatherValue == 5) {
                dayImageView.setImageResource(R.drawable.snowfall_icon); // 눈
            } else {
                dayImageView.setImageResource(R.drawable.loading_logo); // 기본 이미지
            }
        }
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
        calendar24.add(Calendar.HOUR_OF_DAY, 1);
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

                switch (position) {
                    case 0: //서울
                        nx = 60;
                        ny = 126;
                        regId1 = "11B10101";
                        regId2 = "11B00000";
                        api1(nx,ny,regId1,regId2);
                        api2(nx,ny,regId2);
                        fetchWeatherDataAndUpdateTextUI();
                        fetchWeatherDataAndUpdateImageUI();
                        break;
                    case 1: //인천
                        nx = 56;
                        ny = 126;
                        regId1 = "11B20201";
                        regId2 = "11B00000";
                        api1(nx,ny,regId1,regId2);
                        api2(nx,ny,regId2);
                        fetchWeatherDataAndUpdateTextUI();
                        fetchWeatherDataAndUpdateImageUI();
                        break;
                    case 2: //대전
                        nx = 67;
                        ny = 100;
                        regId1 = "11C20401";
                        regId2 = "11C20000";
                        api1(nx,ny,regId1,regId2);
                        api2(nx,ny,regId2);
                        fetchWeatherDataAndUpdateTextUI();
                        fetchWeatherDataAndUpdateImageUI();
                        break;
                    case 3: //대구
                        nx = 89;
                        ny = 90;
                        regId1 = "11H10701";
                        regId2 = "11H10000";
                        api1(nx,ny,regId1,regId2);
                        api2(nx,ny,regId2);
                        fetchWeatherDataAndUpdateTextUI();
                        fetchWeatherDataAndUpdateImageUI();
                        break;
                    case 4: //울산
                        nx = 102;
                        ny = 84;
                        regId1 = "11H20101";
                        regId2 = "11H20000";
                        api1(nx,ny,regId1,regId2);
                        api2(nx,ny,regId2);
                        fetchWeatherDataAndUpdateTextUI();
                        fetchWeatherDataAndUpdateImageUI();
                        break;
                    case 5: //부산
                        nx = 98;
                        ny = 76;
                        regId1 = "11H20201";
                        regId2 = "11H20000";
                        api1(nx,ny,regId1,regId2);
                        api2(nx,ny,regId2);
                        fetchWeatherDataAndUpdateTextUI();
                        fetchWeatherDataAndUpdateImageUI();
                        break;
                    case 6: //광주
                        nx = 58;
                        ny = 74;
                        regId1 = "11B20702";
                        regId2 = "11F20000";
                        api1(nx,ny,regId1,regId2);
                        api2(nx,ny,regId2);
                        fetchWeatherDataAndUpdateTextUI();
                        fetchWeatherDataAndUpdateImageUI();
                        break;
                    case 7: //안양
                        nx = 59;
                        ny = 123;
                        regId1 = "11B20602";
                        regId2 = "11B00000";
                        api1(nx,ny,regId1,regId2);
                        api2(nx,ny,regId2);
                        fetchWeatherDataAndUpdateTextUI();
                        fetchWeatherDataAndUpdateImageUI();
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