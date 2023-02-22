package com.vych.EmployersManagerRest;

import java.time.LocalDateTime;

public class Utils {
    public static LocalDateTime toZeroHour(LocalDateTime dt) {
        return dt.withHour(0).withMinute(0).withSecond(0);
    }
}
