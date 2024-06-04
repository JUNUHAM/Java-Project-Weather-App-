package com.example.api2;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class WeekTem12 {

    private final String baseDate;
    private final String baseTime;
    private final int nx;
    private final int ny;

    public WeekTem12( String baseDate, String baseTime, int nx, int ny) {
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

        NodeList itemList = doc.getElementsByTagName("item");
        String tmxValue = null;
        String tmnValue = null;
        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);
            String category = XmlHelper.getNodeTextContent(item.getElementsByTagName("category").item(0));
            String fcstValue = XmlHelper.getNodeTextContent(item.getElementsByTagName("fcstValue").item(0));

            if (category.equals("TMN")) {
                tmxValue = fcstValue;
            } else if (category.equals("TMX")) {
                tmnValue = fcstValue;
            }

            // TMX와 TMN 값이 모두 존재하는 경우에만 출력하고 변수 초기화
            if (tmxValue != null && tmnValue != null) {
                // 소수점 제거
                int tmxInt = (int) Math.round(Double.parseDouble(tmxValue));
                int tmnInt = (int) Math.round(Double.parseDouble(tmnValue));

                weatherDataBuilder.append(tmxInt).append("℃/").append(tmnInt).append("℃\n");
                tmxValue = null;
                tmnValue = null;
            }
        }

        return weatherDataBuilder.toString();
    }
}
