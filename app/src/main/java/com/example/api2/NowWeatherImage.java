package com.example.api2;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NowWeatherImage {
    private final String baseDate;
    private final String baseTime;
    private final int nx;
    private final int ny;

    public NowWeatherImage(String baseDate, String baseTime, int nx, int ny) { //단기 데이터 이용함
        this.baseDate = baseDate;
        this.baseTime = baseTime;
        this.nx = nx;
        this.ny = ny;
    }

    public int fetchWeatherData() throws Exception {


        String url = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"
                + "?serviceKey=1jdnhESiJyvL8T7ZVy%2FIF%2BLijO8GdJmzjAJptRzoWNgn%2FVAXr%2BP79CxEmEoEGkq1MqFTzFgOjnQWICts87VfmQ%3D%3D"
                + "&pageNo=1"
                + "&numOfRows=1000"
                + "&dataType=XML"
                + "&base_date=" + baseDate
                + "&base_time=" + baseTime
                + "&nx=" + nx
                + "&ny=" + ny;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new URL(url).openStream());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH00", Locale.KOREA);
        String currentDate = dateFormat.format(new Date());

        Calendar calendar = Calendar.getInstance();
        String currentTime;

        calendar.add(Calendar.HOUR_OF_DAY, 1);
        currentTime = timeFormat.format(calendar.getTime());

        int closestTimeIndex = -1;
        long minTimeDifference = Long.MAX_VALUE;
        NodeList itemList = doc.getElementsByTagName("item");
        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);
            String fcstDate = XmlHelper.getNodeTextContent(item.getElementsByTagName("fcstDate").item(0));
            String fcstTime = XmlHelper.getNodeTextContent(item.getElementsByTagName("fcstTime").item(0));

            if (fcstDate.equals(currentDate)) { // 현재 날짜와 같은 데이터만 확인
                long timeDifference = Math.abs(Long.parseLong(fcstTime) - Long.parseLong(currentTime));
                if (timeDifference < minTimeDifference) {
                    minTimeDifference = timeDifference;
                    closestTimeIndex = i;
                }
            }
        }

        int NowWeatherValue = 0;
        if (closestTimeIndex != -1) {
            for (int i = closestTimeIndex; i < itemList.getLength(); i++) {
                Element item = (Element) itemList.item(i);
                String category = XmlHelper.getNodeTextContent(item.getElementsByTagName("category").item(0));
                String fcstValue = XmlHelper.getNodeTextContent(item.getElementsByTagName("fcstValue").item(0));

                if (category.equals("SKY")) {
                    switch (fcstValue) {
                        case "1":
                        case "2":
                            NowWeatherValue = 1; //맑음

                            break;
                        case "3":
                            NowWeatherValue = 2; //구름 많음

                            break;
                        case "4":
                            NowWeatherValue = 3; //흐림

                            break;
                    }
                }
                else if (category.equals("PTY")) {
                    switch (fcstValue) {
                        case "0":
                            NowWeatherValue = 1; //맑음

                            break;
                        case "1":
                        case "2":
                            NowWeatherValue = 4; //비

                            break;
                        case "3":
                        case "4":
                            NowWeatherValue = 5; //눈

                            break;
                    }

                }
            }
        }
        return NowWeatherValue;
    }
}
