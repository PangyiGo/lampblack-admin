package com.osen.cloud.common.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-09-06
 * Time: 16:34
 * Description: 冷链设备传感器参数
 */
@NoArgsConstructor
@AllArgsConstructor
public enum ColdChainSensorCode {

    T01("a01001", "a01001", "监控点温度01"),

    H01("a01002", "a01002", "监控点湿度01"),

    T02("a01001", "T02", "监控点温度02"),

    H02("a01002", "H02", "监控点湿度02"),

    T03("a01001", "T03", "监控点温度03"),

    H03("a01002", "H03", "监控点湿度03"),

    T04("a01001", "T04", "监控点温度04"),

    H04("a01002", "H04", "监控点湿度04"),

    Longitude("", "lng", "经度"),

    Latitude("", "lat", "纬度");

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
