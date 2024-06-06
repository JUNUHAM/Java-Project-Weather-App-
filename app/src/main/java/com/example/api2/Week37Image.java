package com.example.api2;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class Week37Image {
    private final String baseDate;
    private final String baseTime;
    private final String regId;

    public Week37Image(String baseDate, String baseTime, String regId) {
        this.baseDate = baseDate;
        this.baseTime = baseTime;
        this.regId = regId;
    }

    public List<Integer> fetchWeatherData() throws Exception {
        List<Integer> otherdayvalues = new ArrayList<>();

        String url = "https://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst"
                + "?serviceKey=1jdnhESiJyvL8T7ZVy%2FIF%2BLijO8GdJmzjAJptRzoWNgn%2FVAXr%2BP79CxEmEoEGkq1MqFTzFgOjnQWICts87VfmQ%3D%3D"
                + "&pageNo=1"        //페이지 번호
                + "&numOfRows=10" //한 페이지 결과 수
                + "&dataType=XML"    //응답자료형식
                + "&regId=" + regId        //지점번호
                + "&tmFc=" + baseDate + baseTime; //발표시각
        // XML 데이터를 읽어오기
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new URL(url).openStream());

        Calendar calendar = Calendar.getInstance();
        int currentTime = calendar.get(Calendar.HOUR_OF_DAY);


        // 예보지점별 자료 출력
        NodeList itemList = doc.getElementsByTagName("item");
        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);
            //오전,오후 날씨 강수량
            for (int j = 0; j <= 3; j++) {
                String wfAm = XmlHelper.getNodeTextContent(item.getElementsByTagName("wf" + (j+3) + "Am").item(0));
                String wfPm = XmlHelper.getNodeTextContent(item.getElementsByTagName("wf" + (j+3) + "Pm").item(0));
                int otherdayvalue = 0;
                if (currentTime >= 0 && currentTime < 12) {
                    otherdayvalue = getWeatherValue(wfAm);
                } else {
                    otherdayvalue = getWeatherValue(wfPm);
                }
                otherdayvalues.add(otherdayvalue);
            }
        }
        return otherdayvalues;
    }

    private int getWeatherValue(String weather) {
        switch (weather) {
            case "맑음":
            case "구름조금":
                return 1;
            case "구름많음":
                return 2;
            case "흐림":
                return 3;
            case "구름많고 비":
            case "구름많고 비/눈":
            case "흐리고 비":
            case "흐리구 비/눈":
                return 4;
            case "구름많고 눈":
            case "구름많고 눈/비":
            case "흐리고 눈":
            case "흐리구 눈/비":
                return 5;
            default:
                return 0;
        }
    }
}