package com.example.api;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;

public class WeekWeather {
    public static void main(String[] args) throws Exception {
        TodayNow today = new TodayNow();
        Hour618 time = new Hour618();
        // 서비스 키와 URL 설정
		String serviceKey = "1jdnhESiJyvL8T7ZVy%2FIF%2BLijO8GdJmzjAJptRzoWNgn%2FVAXr%2BP79CxEmEoEGkq1MqFTzFgOjnQWICts87VfmQ%3D%3D";
	
	    String url = "https://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst"
	    		+ "?serviceKey=" + serviceKey //서비스키
	            + "&pageNo=1"		//페이지 번호
	            + "&numOfRows=10" //한 페이지 결과 수
	            + "&dataType=XML" 	//응답자료형식                    
	            + "&regId=11B00000"
	            + "&tmFc="+today.formattedDate1+time.getFormattedTime(); // 6시 18시 조회
	    
	    // XML 데이터를 읽어오기
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document doc = builder.parse(new URL(url).openStream());
	    
	    // 예보지점별 자료 출력
		NodeList itemList = doc.getElementsByTagName("item");
		for (int i = 0; i < itemList.getLength(); i++) {
		    Element item = (Element) itemList.item(i);
		    System.out.println("강수확률/기상상황");
		    //오전,오후 날씨 강수량
		    for (int j = 3; j <= 7; j++) {
		    	LocalDate tomorrow = LocalDate.now();
		        LocalDate futureDate = tomorrow.plusDays(j);
		        String rnStAm = XmlHelper.getNodeTextContent(item.getElementsByTagName("rnSt" + j + "Am").item(0));
		        String wfAm = XmlHelper.getNodeTextContent(item.getElementsByTagName("wf" + j + "Am").item(0));
		        String rnStPm = XmlHelper.getNodeTextContent(item.getElementsByTagName("rnSt" + j + "Pm").item(0));
		        String wfPm = XmlHelper.getNodeTextContent(item.getElementsByTagName("wf" + j + "Pm").item(0));
		
		        System.out.println(futureDate+"오전 강수확률 : " + rnStAm + "%, 오전 날씨: " + wfAm);
		        System.out.println(futureDate+"오후 강수확률 : " + rnStPm + "%, 오후날씨: " + wfPm);
		        System.out.println();
	        }

        }
    }
}
	
	
	
