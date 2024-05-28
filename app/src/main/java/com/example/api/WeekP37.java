package com.example.api;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.net.URL;
import java.time.LocalDate;


public class WeekP37 {
    private final String serviceKey;
    private final String baseDate;
    private final String baseTime;

    public WeekP37(String serviceKey, String baseDate, String baseTime) {
        this.serviceKey = serviceKey;
        this.baseDate = baseDate;
        this.baseTime = baseTime;
    }
    public String fetchWeatherData() throws Exception {
        StringBuilder weatherDataBuilder = new StringBuilder(); // StringBuilder 객체 생성

        String url = "https://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst"
                + "?serviceKey=" + serviceKey //서비스키
                + "&pageNo=1"		//페이지 번호
                + "&numOfRows=10" //한 페이지 결과 수
                + "&dataType=XML" 	//응답자료형식
                + "&regId=11B00000"		//지점번호
                + "&tmFc="+baseDate+baseTime; //발표시각
        System.out.println(url);
        // XML 데이터를 읽어오기
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new URL(url).openStream());

        // 예보지점별 자료 출력
        NodeList itemList = doc.getElementsByTagName("item");
        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);
            //오전,오후 날씨 강수량
            for (int j = 3; j <= 7; j++) {
                LocalDate tomorrow = LocalDate.now();
                LocalDate futureDate = tomorrow.plusDays(j);
                String rnStAm = XmlHelper.getNodeTextContent(item.getElementsByTagName("rnSt" + j + "Am").item(0));
                String wfAm = XmlHelper.getNodeTextContent(item.getElementsByTagName("wf" + j + "Am").item(0));
                String rnStPm = XmlHelper.getNodeTextContent(item.getElementsByTagName("rnSt" + j + "Pm").item(0));
                String wfPm = XmlHelper.getNodeTextContent(item.getElementsByTagName("wf" + j + "Pm").item(0));

                weatherDataBuilder.append(rnStAm).append("%").append("/").append(rnStPm).append("%").append("\n");
            }

        }
        return weatherDataBuilder.toString();
    }
}