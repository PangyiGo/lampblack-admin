package com.osen.cloud.system.wechat.requestVo;

import lombok.Data;

/**
 * User: PangYi
 * Date: 2020-01-15
 * Time: 14:06
 * Description:
 */
@Data
public class HistoryDataVo {

    private String deviceNo;

    private String startTime;

    private String endTime;

    private String sensor;
}
