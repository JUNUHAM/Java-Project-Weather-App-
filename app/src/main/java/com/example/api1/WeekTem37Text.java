package com.example.api1;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.net.URL;

public class WeekTem37Text {
    private final String baseDate;
    private final String baseTime;
    private final String regId;

    public WeekTem37Text(String baseDate, String baseTime , String regId) {
        this.baseDate = baseDate;
        this.baseTime = baseTime;
        this.regId= regId;
    }
    public String fetchWeatherData() throws Exception {
        StringBuilder weatherDataBuilder = new StringBuilder(); // StringBuilder 객체 생성

        // 서비스 키와 URL 설정
        String url = "https://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa" //중기기온조회
                + "?serviceKey=1jdnhESiJyvL8T7ZVy%2FIF%2BLijO8GdJmzjAJptRzoWNgn%2FVAXr%2BP79CxEmEoEGkq1MqFTzFgOjnQWICts87VfmQ%3D%3D"
                + "&pageNo=1"		//페이지 번호
                + "&numOfRows=10" //한 페이지 결과 수
                + "&dataType=XML" 	//응답자료형식
                + "&regId="+regId		//지점번호
                + "&tmFc="+baseDate+baseTime; //발표시각
        // XML 데이터를 읽어오기
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new URL(url).openStream());

        // 예보지점별 자료 출력
        NodeList itemList = doc.getElementsByTagName("item");
        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);
            for (int j = 3; j <= 8; j++) {
                String taMax = XmlHelper.getNodeTextContent(item.getElementsByTagName("taMax" + j).item(0));
                String taMin = XmlHelper.getNodeTextContent(item.getElementsByTagName("taMin" + j).item(0));
                weatherDataBuilder.append(taMin).append("℃").append("/").append(taMax).append("℃").append("\n");
            }
        }
        return weatherDataBuilder.toString(); // StringBuilder를 String으로 변환하여 반환
    }
}
