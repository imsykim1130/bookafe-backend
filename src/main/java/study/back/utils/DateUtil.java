package study.back.utils;


import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DateUtil {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 출력 : "yyyy-MM-dd HH:mm:ss" 형태 String
    public static String getDateTime() {
        return formatter.format(LocalDateTime.now());
    }

    // 입력 : "yyyy-MM-dd HH:mm:ss" 형태 String
    // 출력 : "yyyy-MM-dd" 형태 LocalDate
    public static LocalDateTime parseLocalDate(String datetime) throws ParseException {
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(datetime, sdf);
        return localDateTime;
    }
}
