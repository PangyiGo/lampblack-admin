package com.osen.cloud.system.data.util;

import lombok.Data;

/**
 * User: PangYi
 * Date: 2019-10-14
 * Time: 16:02
 * Description: 数据完整率返回VO
 */
@Data
public class ReturnDataIntegrityVO {

    private String deviceNo;

    private String deviceName;

    private long realCount;

    private long real;

    private String realPercentage;

    private long minuteCount;

    private long minute;

    private String minutePercentage;

    private long hourCount;

    private long hour;

    private String hourPercentage;

    private long dayCount;

    private long day;

    private String dayPercentage;
}
