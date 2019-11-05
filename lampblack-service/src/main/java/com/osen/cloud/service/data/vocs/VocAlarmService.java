package com.osen.cloud.service.data.vocs;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.dev_vocs.VocAlarm;

import java.util.List;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:22
 * Description: VOC设备实时数据通用接口层
 */
public interface VocAlarmService extends IService<VocAlarm> {

    /**
     * 插入VOC报警历史数据
     *
     * @param vocAlarm 参数
     */
    void insertAlarm(VocAlarm vocAlarm);

    /**
     * 根据账号获取用户设备报警实时数据
     *
     * @param account 账号
     * @return 信息
     */
    List<VocAlarm> getRealtimeAlarm(String account);

    /**
     * 获取指定设备报警记录
     * 分页
     *
     * @param params 参数
     * @return 信息
     */
    Map<String, Object> getAlarmHistory(Map<String, Object> params);
}
