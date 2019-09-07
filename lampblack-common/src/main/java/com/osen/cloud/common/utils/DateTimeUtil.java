package com.osen.cloud.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * User: PangYi
 * Date: 2019-09-07
 * Time: 10:00
 * Description: 日期时间转换工具
 */
public class DateTimeUtil {

    /**
     * LocalDate 转化 Date
     *
     * @param localDate 日期
     * @return 结果
     */
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime 转化 Date
     *
     * @param localDateTime 日期
     * @return 结果
     */
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date 转化 LocalDate
     *
     * @param date 日期
     * @return 结果
     */
    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date 转化 LocalDateTime
     *
     * @param date 日期
     * @return 结果
     */
    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
