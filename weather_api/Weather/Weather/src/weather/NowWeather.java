package weather;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.net.URL;

public class NowWeather {
    public static void main(String[] args) throws Exception {
    	TodayNow today=new TodayNow();
        // 서비스 키와 URL 설정
        String serviceKey = "1jdnhESiJyvL8T7ZVy%2FIF%2BLijO8GdJmzjAJptRzoWNgn%2FVAXr%2BP79CxEmEoEGkq1MqFTzFgOjnQWICts87VfmQ%3D%3D";
        String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst" //초단기실황조회
                    + "?serviceKey=" + serviceKey //서비스키
                    + "&pageNo=1"		//페이지 번호
                    + "&numOfRows=1000" //한 페이지 결과 수
                    + "&dataType=XML" 	//응답자료형식
                    + "&base_date="+today.formattedDate1 //발표일자
                    + "&base_time="+today.formattedDate2 //발표시각
                    + "&nx=59"		//예보지점 X 좌표
                    + "&ny=125";	//예보지점 Y 좌표

        // XML 데이터를 읽어오기
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new URL(url).openStream());

        // 예보지점별 자료 출력
        NodeList itemList = doc.getElementsByTagName("item");
        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);
            String category = XmlHelper.getNodeTextContent(item.getElementsByTagName("category").item(0));
            String obsrValue = XmlHelper.getNodeTextContent(item.getElementsByTagName("obsrValue").item(0));
         // 카테고리 이름 수정
            String categoryName;
            switch(category) {
                case "PTY":
                    switch (obsrValue) {
                        case "0":
                            obsrValue = "맑음";
                            break;
                        case "1":
                            obsrValue = "비";
                            break;
                        case "2":
                        case "3":
                            obsrValue = "진눈깨비";
                            break;
                        case "4":
                            obsrValue = "눈";
                            break;
                    }
                    categoryName = "기상";
                    break;
                case "REH":
                    categoryName = "습도";
                    break;
                case "RN1":
                    categoryName = "한시간 강수량";
                    break;
                case "T1H":
                    categoryName = "기온";
                    obsrValue += "°C"; 
                    break;
                case "UUU":
                    continue;
                case "VEC":
                    categoryName = "풍향";
                    if (obsrValue.compareTo("337.5") > 0 && obsrValue.compareTo("22.5") < 0) {
                    	obsrValue="북";	
                    }
                    else if (obsrValue.compareTo("22.5") > 0 && obsrValue.compareTo("67.5") < 0) {
                    	obsrValue="북동";	
                    }
                    else if (obsrValue.compareTo("67.5") > 0 && obsrValue.compareTo("112.5") < 0) {
                    	obsrValue="동";	
                    }
                    else if (obsrValue.compareTo("112.5") > 0 && obsrValue.compareTo("157.5") < 0) {
                    	obsrValue="남동";	
                    }
                    else if (obsrValue.compareTo("157.5") > 0 && obsrValue.compareTo("202.5") < 0) {
                    	obsrValue="남";	
                    }
                    else if (obsrValue.compareTo("202.5") > 0 && obsrValue.compareTo("247.5") < 0) {
                    	obsrValue="남서";	
                    }
                    else if (obsrValue.compareTo("247.5") > 0 && obsrValue.compareTo("292.5") < 0) {
                    	obsrValue="서";	
                    }
                    else{
                    	obsrValue="북서";	
                    }
                    break;
                case "VVV":
                	continue;
                case "WSD":
                    categoryName = "풍속";
                    obsrValue += "m/s";
                    break;
                default:
                    categoryName = "Unknown"; // 기타 경우 처리
                    break;
            }

            System.out.print(categoryName+" : ");
            System.out.println(obsrValue);
        }
    }
}