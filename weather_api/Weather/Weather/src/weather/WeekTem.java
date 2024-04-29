package weather;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.net.URL;

public class WeekTem {
    public static void main(String[] args) throws Exception {
    	TodayNow today=new TodayNow();
    	Hour618 time=new Hour618();
        
        // 서비스 키와 URL 설정
        String serviceKey = "1jdnhESiJyvL8T7ZVy%2FIF%2BLijO8GdJmzjAJptRzoWNgn%2FVAXr%2BP79CxEmEoEGkq1MqFTzFgOjnQWICts87VfmQ%3D%3D";
        String url = "https://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa" //중기기온조회
                    + "?serviceKey=" + serviceKey //서비스키
                    + "&pageNo=1"		//페이지 번호
                    + "&numOfRows=10" //한 페이지 결과 수
                    + "&dataType=XML" 	//응답자료형식                    
                    + "&regId=11B10101"		//지점번호
                    + "&tmFc="+today.formattedDate1+time.getFormattedTime(); //발표시각

        // XML 데이터를 읽어오기
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new URL(url).openStream());

        // 예보지점별 자료 출력
        NodeList itemList = doc.getElementsByTagName("item");
        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);
    			System.out.println("최고/최저");
            	for (int j = 3; j <= 10; j++) {
            		String taMax = XmlHelper.getNodeTextContent(item.getElementsByTagName("taMax" + j).item(0));
            		String taMin = XmlHelper.getNodeTextContent(item.getElementsByTagName("taMin" + j).item(0));
            		System.out.println(j+"일 후 "+taMax+"/"+taMin);
            	}
            
            
        }
    }
}