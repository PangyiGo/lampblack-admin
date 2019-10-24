package com.osen.cloud.common.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-09-06
 * Time: 16:34
 * Description: VOC设备传感器参数
 */
@NoArgsConstructor
@AllArgsConstructor
public enum VocSensorCode {

    VOC("a24088", "Voc", "TVOC"),

    FLOW("a42006", "Flow", "流量"),

    SPEED("a01011", "S02", "烟气流速"),

    PRESSURE("a01006", "P01", "压力"),

    TEMP("a01001", "T01", "温度");

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
