package com.example.api2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class exToday {
    public String getFormattedDate() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        int currentHour = currentDateTime.getHour();

        // 현재 시간이 0시부터 6시 사이인 경우 어제의 날짜를 반환
        if (currentHour >= 0 && currentHour < 6) {
            LocalDate yesterday = LocalDate.now().minusDays(1);
            return yesterday.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        } else { // 그 외의 경우에는 오늘의 날짜를 반환
            return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
    }
}

