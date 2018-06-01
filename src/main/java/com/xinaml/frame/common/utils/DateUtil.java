package com.xinaml.frame.common.utils;

import com.xinaml.frame.common.custom.constant.CommonConst;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public final class DateUtil {
    private static final DateTimeFormatter DATETIME = DateTimeFormatter.ofPattern(CommonConst.DATETIME);
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern(CommonConst.DATE);
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern(CommonConst.TIME);
    private static Calendar calendar = null;

    /**
     * 日期时间转换
     *
     * @param dateTime
     * @return
     */
    public static LocalDateTime parseDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DATETIME);
    }

    /**
     * 日期转换
     *
     * @param date
     * @return
     */

    public static LocalDate parseDate(String date) {
        return LocalDate.parse(date, DATE);
    }

    /**
     * 时间转换
     *
     * @param time
     * @return
     */
    public static LocalTime parseTime(String time) {
        return LocalTime.parse(time, TIME);
    }


    /**
     * 日期时间转相应字符串
     *
     * @param date (LocalDate,LocalTime,LocalDateTime)
     * @return String
     */
    public static <DATE> String dateToString(DATE date) {
        if (date.getClass().equals(LocalDate.class)) {
            return ((LocalDate) date).format(DATE);
        } else if (date.getClass().equals(LocalDateTime.class)) {
            return ((LocalDateTime) date).format(DATETIME);
        } else {
            return ((LocalTime) date).format(TIME);
        }
    }


    public static LocalDateTime parseTime(long time) {
        Instant instant = Instant.ofEpochMilli(time);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }
    public static  LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return  LocalDateTime.ofInstant(instant, zone);
    }
}
