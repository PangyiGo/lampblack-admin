package com.osen.cloud.common.utils;

/**
 * User: PangYi
 * Date: 2019-10-29
 * Time: 9:53
 * Description: 数据表工具类
 */
public class TableUtil {

    /*
     * VOC设备数据表名称
     */
    public static String VocHistory = "voc_history";

    public static String VocMinute = "voc_minute";

    public static String VocHour = "voc_hour";

    public static String VocDay = "voc_day";

    /*
     * 冷链设备数据表名称
     */
    public static String ColdHistory = "coldchain_history";

    public static String ColdMinute = "coldchain_minute";

    public static String ColdHour = "coldchain_hour";

    public static String ColdDay = "coldchain_day";

    /**
     * VOC数据缓存键名称
     */
    public static String Voc_RealTime = "voc_realtime_db";

    public static String Voc_Conn = "voc_connection_db";

    public static String Voc_Alarm = "voc_alarm_db";

    /**
     * 冷链数据缓存键名称
     */
    public static String Cold_RealTime = "coldchain_realtime_db";

    public static String Cold_Conn = "coldchain_connection_db";

    public static String Cold_Alarm = "coldchain_alarm_db";
}
