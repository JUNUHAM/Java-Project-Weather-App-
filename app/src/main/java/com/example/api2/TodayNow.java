package com.example.api2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TodayNow {
    // 년월일
    private LocalDate currentDate = LocalDate.now();
    private DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMdd");
    public String formattedDate1 = currentDate.format(formatter1);

    // 시간분
    private LocalDateTime currentDateTime = LocalDateTime.now();
    private DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH00");
    public String formattedDate2 = currentDateTime.format(formatter2);

    // formattedDate1에 대한 getter 메서드
    public String getFormattedDate1() {
        return formattedDate1;
    }

    // formattedDate2에 대한 getter 메서드
    public String getFormattedDate2() {
        return formattedDate2;
    }
}