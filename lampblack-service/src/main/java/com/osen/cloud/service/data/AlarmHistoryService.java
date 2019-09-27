package com.osen.cloud.service.data;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.AlarmHistory;

import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 11:25
 * Description: 设备报警历史记录服务接口
 */
public interface AlarmHistoryService extends IService<AlarmHistory> {

    /**
     * 插入报警数据
     *
     * @param alarmHistory 数据
     */
    void insertAlarmData(AlarmHistory alarmHistory);

    /**
     * 查询指定设备号报警历史记录
     *
     * @param params 参数
     * @return 信息
     */
    Map<String, Object> queryAlarmHistoryByAccount(Map<String, Object> params);
}
