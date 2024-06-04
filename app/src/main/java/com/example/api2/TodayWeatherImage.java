package com.example.api2;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class TodayWeatherImage {
    private final String baseDate;
    private final String baseTime;
    private final int nx;
    private final int ny;

    public TodayWeatherImage(String baseDate, String baseTime, int nx, int ny) {
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

        NodeList itemList = doc.getElementsByTagName("item");
        int NowWeatherValue = 0;
        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);
            String category = XmlHelper.getNodeTextContent(item.getElementsByTagName("category").item(0));
            String fcstValue = XmlHelper.getNodeTextContent(item.getElementsByTagName("fcstValue").item(0));

            if (category.equals("SKY")) {
                if(fcstValue.equals("1") || fcstValue.equals("2")){
                    NowWeatherValue=1; //맑음
                }
                else if(fcstValue.equals("3")){
                    NowWeatherValue=2; //구름 많음
                }
                else if(fcstValue.equals("4")){
                    NowWeatherValue=3; //흐림
                }
            }
            else if (category.equals("PTY")) {
                if(fcstValue.equals("0")){
                    NowWeatherValue=1; //맑음
                }
                else if(fcstValue.equals("1") || fcstValue.equals("2")){
                    NowWeatherValue=4; //비
                }
                else if(fcstValue.equals("3") || fcstValue.equals("4")){
                    NowWeatherValue=5; //눈
                }
                
            }
        }

        return NowWeatherValue;
    }
}