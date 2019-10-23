package com.osen.cloud.service.data;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.dev_lampblack.AlarmRealtime;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 11:25
 * Description: 设备报警实时数据上传服务接口
 */
public interface AlarmRealtimeService extends IService<AlarmRealtime> {

    /**
     * 查询指定用户实时报警记录
     *
     * @param account 账号
     * @return 信息
     */
    List<AlarmRealtime> findAlarmTOAccount(String account);
}
