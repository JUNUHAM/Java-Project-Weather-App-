package com.example.api;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalTime;

public class TodayWeather {
    public static void main(String[] args) throws Exception {
        TodayNow today = new TodayNow();
        Hour514 time = new Hour514();
        // 서비스 키와 URL 설정
        String serviceKey = "1jdnhESiJyvL8T7ZVy%2FIF%2BLijO8GdJmzjAJptRzoWNgn%2FVAXr%2BP79CxEmEoEGkq1MqFTzFgOjnQWICts87VfmQ%3D%3D";
        String numOfRows = "809"; // 한 번에 조회할 행 수
        int page=1;
        
            String url = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"
                        + "?serviceKey=" + serviceKey
                        + "&pageNo=" + page
                        + "&numOfRows=" + numOfRows
                        + "&dataType=XML"
                        + "&base_date=" + today.formattedDate1
                        + "&base_time=" +time.getFormattedTime() // 02시 기준으로 3시간마다
                        + "&nx=55"
                        + "&ny=127";

        // XML 데이터를 읽어오기
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new URL(url).openStream());
        
        // 예보지점별 자료 출력
        NodeList itemList = doc.getElementsByTagName("item");
        String prevFcstDate = null;
        String prevFcstTime = null;
        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);
            String category = XmlHelper.getNodeTextContent(item.getElementsByTagName("category").item(0));
            String fcstValue = XmlHelper.getNodeTextContent(item.getElementsByTagName("fcstValue").item(0));
            String fcstDate = XmlHelper.getNodeTextContent(item.getElementsByTagName("fcstDate").item(0));
            String fcstTime = XmlHelper.getNodeTextContent(item.getElementsByTagName("fcstTime").item(0));
            // 현재 fcstDate가 이전과 같지 않을 때만 출력
            if (!fcstDate.equals(prevFcstDate)) {
            	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy년MM월dd일");
                LocalDate parsedDate = LocalDate.parse(fcstDate, DateTimeFormatter.BASIC_ISO_DATE);
                System.out.println(parsedDate.format(dateFormatter));
            }
            
            // 현재 fcstTime이 이전과 같지 않을 때만 출력
            if (!fcstTime.equals(prevFcstTime)) {
                LocalTime parsedTime = LocalTime.parse(fcstTime, DateTimeFormatter.ofPattern("HHmm"));
                System.out.println(parsedTime);
            }
            // 카테고리 이름 수정
            String categoryName;
            switch(category) {
                case "TMP":
                	categoryName="현재 온도";
                	fcstValue+="°C";
                    break;
                case "TMX":
                	categoryName="일 최고기온";
                	fcstValue+="°C";
                    break;
                case "TMN":
                	categoryName="일 최저기온";
                	fcstValue+="°C";
                    break;
                case "UUU":
                    continue;
                case "VVV":
                	continue;
                case "VEC":
                    categoryName = "풍향";
                    if (fcstValue.compareTo("337.5") > 0 && fcstValue.compareTo("22.5") < 0) {
                    	fcstValue="북";	
                    }
                    else if (fcstValue.compareTo("22.5") > 0 && fcstValue.compareTo("67.5") < 0) {
                    	fcstValue="북동";	
                    }
                    else if (fcstValue.compareTo("67.5") > 0 && fcstValue.compareTo("112.5") < 0) {
                    	fcstValue="동";	
                    }
                    else if (fcstValue.compareTo("112.5") > 0 && fcstValue.compareTo("157.5") < 0) {
                    	fcstValue="남동";	
                    }
                    else if (fcstValue.compareTo("157.5") > 0 && fcstValue.compareTo("202.5") < 0) {
                    	fcstValue="남";	
                    }
                    else if (fcstValue.compareTo("202.5") > 0 && fcstValue.compareTo("247.5") < 0) {
                    	fcstValue="남서";	
                    }
                    else if (fcstValue.compareTo("247.5") > 0 && fcstValue.compareTo("292.5") < 0) {
                    	fcstValue="서";	
                    }
                    else{
                    	fcstValue="북서";	
                    }
                    break;
                case "WSD":
                    categoryName = "풍속";
                    fcstValue += "m/s"; 
                    break;
                case "SKY":
                	categoryName = "구름 양";
	                	switch (fcstValue) {
	                    case "0":
	                        fcstValue = "맑음";
	                        break;
	                    case "1":
	                        fcstValue = "구름조금";
	                        break;
	                    case "2":
	                    case "3":
	                        fcstValue = "구름많음";
	                        break;
	                    case "4":
	                        fcstValue = "흐림";
	                        break;
                }    
	             break;
                case "PTY":
                	categoryName = "강수형태";
	                	switch (fcstValue) {
	                    case "0":
	                        fcstValue = "맑음";
	                        break;
	                    case "1":
	                        fcstValue = "비";
	                        break;
	                    case "2":
	                    case "3":
	                        fcstValue = "진눈깨비";
	                        break;
	                    case "4":
	                        fcstValue = "눈";
	                        break;
                }
                    break;
                case "POP":
                    categoryName = "강수확률";
                    fcstValue += "%";
                    break;
                case "REH" :
                	categoryName = "습도";
                    fcstValue += "%";
                case "WAV":
                    continue;
                case "PCP":
                	if(fcstValue.equals("강수없음")) {
                		continue;
                	}
                	else if(fcstValue.contains("미만")) {
                    	categoryName="강수량";
                        break;
                    	}
                	else {
                		categoryName="강수량";
                        fcstValue += "mm";
                    break;
                	}
                case "SNO":
                	if(fcstValue.equals("적설없음")) {
                	continue;
                	}
                	else if(fcstValue.contains("미만")) {
                    	categoryName="적설량";
                        break;
                    	}
                	else {
                	categoryName="적설량";
                    fcstValue += "mm";
                    break;
                	}
                default:
                    categoryName = category; // 기타 경우 처리
                    break;
            }
            prevFcstDate = fcstDate;
            prevFcstTime = fcstTime;
            System.out.println(categoryName + " : " + fcstValue);
        }
    }
}


