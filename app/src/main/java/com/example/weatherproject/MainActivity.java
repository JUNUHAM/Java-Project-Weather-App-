package com.example.weatherproject;

import com.example.api1.*;
import com.example.api2.NowWeatherImage;
import com.example.api2.TodayWeatherImage;
import com.example.api2.Week1Image;
import com.example.api2.Week2Image;
import com.example.api2.Week37Image;
import com.example.weatherproject.databinding.ActivityMainBinding;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
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
    private ActivityMainBinding binding;

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
    private int nx = 60;
    private int ny = 126;
    private String regId1 = "11B10101";
    private String regId2 = "11B10101";

    private void api1(int nx, int ny, String regId1, String regId2) {
        TodayNow today = new TodayNow();
        exToday extoday = new exToday();
        Hour514 time5 = new Hour514();
        Hour618 time6 = new Hour618();
        nowWeathertext = new NowWeatherText(today.formattedDate1, today.formattedDate2, nx, ny);
        todayWeathertext = new TodayWeatherText(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
        weektem12text = new WeekTem12Text(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
        weektem37text = new WeekTem37Text(extoday.getFormattedDate(), time6.getFormattedTime(), regId1);
        weekp12text = new WeekP12Text(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
        weekp37text = new WeekP37Text(extoday.getFormattedDate(), time6.getFormattedTime(), regId2);
    }

    private void api2(int nx, int ny, String regId2) {
        exToday extoday = new exToday();
        Hour514 time5 = new Hour514();
        Hour618 time6 = new Hour618();
        nowweatherimage = new NowWeatherImage(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
        todayweatherimage = new TodayWeatherImage(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
        week1image = new Week1Image(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
        week2image = new Week2Image(extoday.getFormattedDate(), time5.getFormattedTime(), nx, ny);
        week37image = new Week37Image(extoday.getFormattedDate(), time6.getFormattedTime(), regId2);
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
                    binding.nowTem.setText(nowweathertextData);

                    for (int i = 0; i <= 6; i++) {
                        TextView dayTemTextView = null;
                        switch (i) {
                            case 0:
                                dayTemTextView = binding.daytem1;
                                break;
                            case 1:
                                dayTemTextView = binding.daytem2;
                                break;
                            case 2:
                                dayTemTextView = binding.daytem3;
                                break;
                            case 3:
                                dayTemTextView = binding.daytem4;
                                break;
                            case 4:
                                dayTemTextView = binding.daytem5;
                                break;
                            case 5:
                                dayTemTextView = binding.daytem6;
                                break;
                            case 6:
                                dayTemTextView = binding.daytem7;
                                break;

                        }
                        if (dayTemTextView != null) {
                            if (i < todayweatherDataArray.length) {
                                dayTemTextView.setText(todayweatherDataArray[i]);
                            }
                        }
                    }

                    for (int i = 0; i <= 1; i++) {
                        TextView weekTemTextView12 = null;
                        switch (i) {
                            case 0:
                                weekTemTextView12 = binding.weektem1;
                                break;
                            case 1:
                                weekTemTextView12 = binding.weektem2;
                                break;
                        }
                        if (weekTemTextView12 != null) {
                            weekTemTextView12.setText(weatherDataArray12[i]);
                        }
                    }

                    for (int i = 0; i <= 3; i++) {
                        TextView weekTemTextView37 = null;
                        switch (i) {
                            case 0:
                                weekTemTextView37 = binding.weektem3;
                                break;
                            case 1:
                                weekTemTextView37 = binding.weektem4;
                                break;
                            case 2:
                                weekTemTextView37 = binding.weektem5;
                                break;
                            case 3:
                                weekTemTextView37 = binding.weektem6;
                                break;

                        }
                        if (weekTemTextView37 != null) {
                            weekTemTextView37.setText(weatherDataArray37[i]);
                        }
                    }

                    for (int i = 0; i <= 1; i++) {
                        TextView weekperTextView12 = null;
                        switch (i) {
                            case 0:
                                weekperTextView12 = binding.weekp1;
                                break;
                            case 1:
                                weekperTextView12 = binding.weekp2;
                                break;
                        }
                        if (weekperTextView12 != null) {
                            weekperTextView12.setText(PercentDataArray12[i]);
                        }
                    }

                    for (int i = 0; i <= 3; i++) {
                        TextView weekperTextView37 = null;
                        switch (i) {
                            case 0:
                                weekperTextView37 = binding.weekp3;
                                break;
                            case 1:
                                weekperTextView37 = binding.weekp4;
                                break;
                            case 2:
                                weekperTextView37 = binding.weekp5;
                                break;
                            case 3:
                                weekperTextView37 = binding.weekp6;
                                break;
                        }
                        if (weekperTextView37 != null) {
                            weekperTextView37.setText(PercentDataArray37[i]);
                        }
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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 초기 UI 설정
        initializeUI();

        // 시간 및 날짜 설정 비동기 처리
        new Handler().postDelayed(this::initializeTimeAndDate, 0);

        // 도시 선택 처리 비동기 처리
        new Handler().postDelayed(this::initializeCitySelection, 0);
    }

    private void initializeUI() {
        // Inset 처리
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cityName = findViewById(R.id.cityName);
        Spinner spinner = findViewById(R.id.my_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itmes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(0);
    }

    private void initializeTimeAndDate() {
        // 시간 처리
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM월 dd일 HH시", Locale.KOREA);
        String currentTime = dateFormat.format(calendar.getTime());

        // Nowtime TextView 식별
        TextView nowTimeTextView = findViewById(R.id.Nowtime);

        // 현재 시간 설정
        nowTimeTextView.setText(currentTime);

        // 시간 처리
        Calendar calendar24 = Calendar.getInstance();
        SimpleDateFormat dateFormat24 = new SimpleDateFormat("HH", Locale.KOREA);
        calendar24.add(Calendar.HOUR_OF_DAY, 1);
        String currentTime24 = dateFormat24.format(calendar24.getTime());

        // time241부터 time247까지의 TextView를 반복문으로 설정
        for (int i = 241; i <= 247; i++) {
            int textViewId = getResources().getIdentifier("time" + i, "id", getPackageName());
            TextView timeTextView = findViewById(textViewId);
            timeTextView.setText(currentTime24 + "시");
            calendar24.add(Calendar.HOUR_OF_DAY, 1);
            currentTime24 = dateFormat24.format(calendar24.getTime());
        }

        // 일 처리
        Calendar calendar7 = Calendar.getInstance();
        calendar7.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat dateFormat7 = new SimpleDateFormat("dd", Locale.KOREA);
        String currentTime7 = dateFormat7.format(calendar7.getTime());

        for (int i = 72; i <= 77; i++) {
            int textViewId = getResources().getIdentifier("time" + i, "id", getPackageName());
            TextView timeTextView = findViewById(textViewId);
            timeTextView.setText(currentTime7 + "일");
            calendar7.add(Calendar.DAY_OF_MONTH, 1);
            currentTime7 = dateFormat7.format(calendar7.getTime());
        }
    }

    private void initializeCitySelection() {
        Spinner spinner = findViewById(R.id.my_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityName.setText(itmes[position]);

                switch (position) {
                    case 0: //서울
                        setCityData(60, 126, "11B10101", "11B00000");
                        break;
                    case 1: //인천
                        setCityData(56, 126, "11B20201", "11B00000");
                        break;
                    case 2: //대전
                        setCityData(67, 100, "11C20401", "11C20000");
                        break;
                    case 3: //대구
                        setCityData(89, 90, "11H10701", "11H10000");
                        break;
                    case 4: //울산
                        setCityData(102, 84, "11H20101", "11H20000");
                        break;
                    case 5: //부산
                        setCityData(98, 76, "11H20201", "11H20000");
                        break;
                    case 6: //광주
                        setCityData(58, 74, "11B20702", "11F20000");
                        break;
                    case 7: //안양
                        setCityData(59, 123, "11B20602", "11B00000");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cityName.setText("도시 선택");
            }
        });
    }

    private void setCityData(int nx, int ny, String regId1, String regId2) {
        api1(nx, ny, regId1, regId2);
        api2(nx, ny, regId2);
        fetchWeatherDataAndUpdateTextUI();
        fetchWeatherDataAndUpdateImageUI();
    }
}