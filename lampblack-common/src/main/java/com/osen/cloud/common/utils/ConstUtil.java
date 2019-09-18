package com.osen.cloud.common.utils;

import java.time.LocalDate;

/**
 * User: PangYi
 * Date: 2019-08-30
 * Time: 10:08
 * Description: 全局静态变量
 */
public class ConstUtil {

    // 用户初始密码，12345678
    public static final String INIT_PASSWORD = "$2a$10$yNCkJq5IlAVX07aK2eD2kOoxZhxz7ym.FKjaTK7MimhMRASiUvfkS";

    // 监听端口
    public static final Integer SERVER_PORT = 8888;

    // 日期格式化
    public static String DATE_FORMAT = "yyyyMMddHHmmss";

    // 日期格式化
    public static String DATETIME_FORMAT = "yy-MM-dd HH:mm:ss.SSS";

    // 连接主键key
    public static String DATA_KEY = "DataRealtime_DB";

    // 设备主键key
    public static String DEVICE_KEY = "DeviceNo_DB";

    // 报警主键key
    public static String ALARM_KEY = "AlarmRealtime_DB";

    // 关闭状态
    public static Integer CLOSE_STATUS = 1;

    // 开合状态
    public static Integer OPEN_STATUS = 2;

    // 实时数据表
    public static String REALTIME_TB = "data_history";

    // 分钟数据表
    public static String MINUTE_TB = "data_minute";

    // 小时数据表
    public static String HOUR_TB = "data_hour";

    // 每天数据表
    public static String DAY_TB = "data_day";

    /**
     * 动态生成表名
     *
     * @param tableName 原表名
     * @return 新表名
     */
    public static String createNewTableName(String tableName) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int monthValue = now.getMonthValue();
        String month = "";
        if (monthValue < 10)
            month = "0" + monthValue;
        // 格式：基本表明_年月
        String newTable = tableName + "_" + year + month;
        System.out.println(newTable);
        return newTable;
    }

}
