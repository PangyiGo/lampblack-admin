package com.osen.cloud.common.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-09-06
 * Time: 16:34
 * Description: 设备传感器参数
 */
@NoArgsConstructor
@AllArgsConstructor
public enum SensorCode {

    LAMPBLACK("a00000", "LAMPBLACK", "油烟浓度"),

    PM("a34002", "PM", "颗粒物浓度"),

    NMHC("a34004", "NMHC", "非甲烷总烃浓度");

    /**
     * 传感器参数编号
     */
    private String code;

    /**
     * 传感器参数名称
     */
    private String name;

    /**
     * 参数说明
     */
    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
