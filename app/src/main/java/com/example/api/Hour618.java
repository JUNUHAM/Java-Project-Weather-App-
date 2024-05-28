package com.example.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Hour618 {
    TodayNow today = new TodayNow();
    private int[] allowedHours = {6, 18}; // 6시와 18시만 허용
    private LocalDateTime currentDateTime;

    public Hour618() {
        this.currentDateTime = LocalDateTime.now().withSecond(0).withNano(0);
    }

    public void roundToNearestAllowedHour() {
        int currentHour = currentDateTime.getHour();
        // 현재 시간이 0시부터 6시 사이인 경우 18시로 설정
        if (currentHour >= 0 && currentHour < 6) {
            currentDateTime = currentDateTime.withHour(18);
        } else { // 그 외의 경우에는 가장 가까운 시간으로 설정
            int nearestHour = allowedHours[0];
            for (int hour : allowedHours) {
                if (currentHour >= hour) {
                    nearestHour = hour;
                } else {
                    break;
                }
            }
            currentDateTime = currentDateTime.withHour(nearestHour);
        }
    }

    public String getFormattedTime() {
        roundToNearestAllowedHour(); // 반올림 수행
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH00");
        return currentDateTime.format(formatter);
    }
}
