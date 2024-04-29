package weather;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Hour514 {
	TodayNow today = new TodayNow();
    private int[] allowedHours = {5, 14}; // 6시와 18시만 허용
    private LocalDateTime currentDateTime;

    public Hour514() {
        this.currentDateTime = LocalDateTime.now().withSecond(0).withNano(0);
    }

    public void roundToNearestAllowedHour() {
        int currentHour = currentDateTime.getHour();
        int nearestHour = allowedHours[0];
        for (int hour : allowedHours) {
            if (currentHour >= hour) {
                nearestHour = hour;
            } else {
                break;
            }
        }
        currentDateTime = currentDateTime.withHour(nearestHour);
    }

    public String getFormattedTime() {
        roundToNearestAllowedHour(); // 반올림 수행
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH00");
        return currentDateTime.format(formatter);
    }

    public void printNextHours(int numHours) {
        for (int i = 0; i < numHours; i++) {
            System.out.println(getFormattedTime());
        }
    }
}