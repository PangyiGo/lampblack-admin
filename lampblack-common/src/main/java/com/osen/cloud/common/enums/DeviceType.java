package com.osen.cloud.common.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-10-25
 * Time: 15:40
 * Description: 设备类型
 */
@NoArgsConstructor
@AllArgsConstructor
public enum DeviceType {

    // 油烟设备名称
    Lampblack("lampblack"),

    // VOC设备名称
    Voc("voc"),

    // 冷链设备名称
    ColdChain("coldchain");

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
