package weather;

public class XmlAddress {
	//년월일 시각
	TodayNow todayNow=new TodayNow();
	
	 // 서비스 키와 URL 설정
    String serviceKey = "1jdnhESiJyvL8T7ZVy%2FIF%2BLijO8GdJmzjAJptRzoWNgn%2FVAXr%2BP79CxEmEoEGkq1MqFTzFgOjnQWICts87VfmQ%3D%3D";
    String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst" //초단기실황조회
                + "?serviceKey=" + serviceKey //서비스키
                + "&pageNo=1"		//페이지 번호
                + "&numOfRows=1000" //한 페이지 결과 수
                + "&dataType=XML" 	//응답자료형식
                + "&base_date="+todayNow.formattedDate1 //발표일자
                + "&base_time="+todayNow.formattedDate2 //발표시각
                + "&nx=59"		//예보지점 X 좌표
                + "&ny=123";	//예보지점 Y 좌표

}
