package com.osen.cloud.common.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * User: PangYi
 * Date: 2019-10-31
 * Time: 10:25
 * Description: 设备数据表月份
 */
@NoArgsConstructor
@AllArgsConstructor
public enum MonthCode {

    Lampblack(8),

    Voc(9),

    ColdChain(9);

    private int month;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
