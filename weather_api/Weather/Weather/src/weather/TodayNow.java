package weather;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TodayNow{
	// 년월일
	LocalDate currentDate = LocalDate.now();
	DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMdd");
	String formattedDate1 = currentDate.format(formatter1);
    
    // 시간분
    LocalDateTime currentDateTime = LocalDateTime.now();
    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH00");
    String formattedDate2 = currentDateTime.format(formatter2);
}
