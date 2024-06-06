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

public class Week1Image {
    private final String baseDate;
    private final String baseTime;
    private final int nx;
    private final int ny;

    public Week1Image(String baseDate, String baseTime, int nx, int ny) {
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


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        String currentDate = dateFormat.format(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1); // 내일 날짜 계산
        String tomorrowDate = dateFormat.format(calendar.getTime());

        int tomorrowvalue=0;
        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);
            String category = XmlHelper.getNodeTextContent(item.getElementsByTagName("category").item(0));
            String fcstValue = XmlHelper.getNodeTextContent(item.getElementsByTagName("fcstValue").item(0));
            String fcstDate = XmlHelper.getNodeTextContent(item.getElementsByTagName("fcstDate").item(0));

            if (fcstDate.equals(tomorrowDate)) {
                if (category.equals("SKY")) {
                    if (fcstValue.equals("1") || fcstValue.equals("2")) {
                        tomorrowvalue = 1; //맑음
                    } else if (fcstValue.equals("3")) {
                        tomorrowvalue = 2; //구름 많음
                    } else if (fcstValue.equals("4")) {
                        tomorrowvalue = 3; //흐림
                    }
                } else if (category.equals("PTY")) {
                    if (fcstValue.equals("0")) {
                        tomorrowvalue = 1; //맑음
                    } else if (fcstValue.equals("1") || fcstValue.equals("2")) {
                        tomorrowvalue = 4; //비
                    } else if (fcstValue.equals("3") || fcstValue.equals("4")) {
                        tomorrowvalue = 5; //눈
                    }
                }
            }
            /*if (fcstDate.equals(day3Date)) {
                if (category.equals("SKY")) {
                    if (fcstValue.equals("1") || fcstValue.equals("2")) {
                        day3value = 1; //맑음
                    } else if (fcstValue.equals("3")) {
                        day3value = 2; //구름 많음
                    } else if (fcstValue.equals("4")) {
                        day3value = 3; //흐림
                    }
                } else if (category.equals("PTY")) {
                    if (fcstValue.equals("0")) {
                        day3value = 1; //맑음
                    } else if (fcstValue.equals("1") || fcstValue.equals("2")) {
                        day3value = 4; //비
                    } else if (fcstValue.equals("3") || fcstValue.equals("4")) {
                        day3value = 5; //눈
                    }
                }

            }*/
        }
        return tomorrowvalue;
    }
}
