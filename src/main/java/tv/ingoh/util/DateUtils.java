package tv.ingoh.util;

import java.time.LocalDateTime;

public class DateUtils {

    public static boolean isAfterAprilFools2022() {
        return LocalDateTime.now().isAfter(LocalDateTime.of(2022, 4, 1, 0, 0));
    }
    
}
