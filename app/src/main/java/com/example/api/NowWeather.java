package weather;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.net.URL;

public class NowWeather {
    private final String serviceKey;
    private final String baseDate;
    private final String baseTime;
    private final int nx;
    private final int ny;

    public NowWeather(String serviceKey, String baseDate, String baseTime, int nx, int ny) {
        this.serviceKey = serviceKey;
        this.baseDate = baseDate;
        this.baseTime = baseTime;
        this.nx = nx;
        this.ny = ny;
    }

    public String fetchWeatherData() throws Exception {
        StringBuilder weatherDataBuilder = new StringBuilder(); // StringBuilder 객체 생성

        String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"
                + "?serviceKey=" + serviceKey
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
        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);
            String category = XmlHelper.getNodeTextContent(item.getElementsByTagName("category").item(0));
            String obsrValue = XmlHelper.getNodeTextContent(item.getElementsByTagName("obsrValue").item(0));

            	if(category.equals("T1H")){
                weatherDataBuilder.append(obsrValue).append("℃");
            	}
            
        }

        return weatherDataBuilder.toString(); // StringBuilder를 String으로 변환하여 반환
    }
}