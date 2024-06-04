package com.example.api1;

import com.example.api2.XmlHelper;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class TodayWeatherText {
    private final String baseDate;
    private final String baseTime;
    private final int nx;
    private final int ny;

    public TodayWeatherText(String baseDate, String baseTime, int nx, int ny) {
        this.baseDate = baseDate;
        this.baseTime = baseTime;
        this.nx = nx;
        this.ny = ny;
    }

    public String fetchWeatherData() throws Exception {
        StringBuilder weatherDataBuilder = new StringBuilder();

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
            String fcstDate = com.example.api2.XmlHelper.getNodeTextContent(item.getElementsByTagName("fcstDate").item(0));
            String fcstTime = com.example.api2.XmlHelper.getNodeTextContent(item.getElementsByTagName("fcstTime").item(0));

            if (fcstDate.equals(currentDate)) { // 현재 날짜와 같은 데이터만 확인
                long timeDifference = Math.abs(Long.parseLong(fcstTime) - Long.parseLong(currentTime));
                if (timeDifference < minTimeDifference) {
                    minTimeDifference = timeDifference;
                    closestTimeIndex = i;
                }
            }
        }

        if (closestTimeIndex != -1) {
            for (int i = closestTimeIndex; i < itemList.getLength(); i++) {
                Element item = (Element) itemList.item(i);
                String category = com.example.api2.XmlHelper.getNodeTextContent(item.getElementsByTagName("category").item(0));
                String fcstValue = XmlHelper.getNodeTextContent(item.getElementsByTagName("fcstValue").item(0));

                if (category.equals("TMP")) {
                    weatherDataBuilder.append(fcstValue).append("℃").append("\n");
                }
            }
        }
        return weatherDataBuilder.toString();
    }
}
