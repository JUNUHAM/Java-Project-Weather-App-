package com.example.weatherproject;

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

public class MainActivity extends AppCompatActivity {

    TextView cityName;

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH시", Locale.KOREA);
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
    }
}