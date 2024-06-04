package com.example.api2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Hour514 {
    private int[] allowedHours = {5, 14}; // 5시와 14시만 허용
    private LocalDateTime currentDateTime;

    public Hour514() {
        this.currentDateTime = LocalDateTime.now().withSecond(0).withNano(0);
    }

    public void roundToNearestAllowedHour() {
        int currentHour = currentDateTime.getHour();
        // 현재 시간이 00시부터 05시 사이인 경우 14시로 설정
        if (currentHour >= 0 && currentHour < 5) {
            currentDateTime = currentDateTime.withHour(14);
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

    public void printNextHours(int numHours) {
        for (int i = 0; i < numHours; i++) {
            System.out.println(getFormattedTime());
        }
    }
}