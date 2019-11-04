package com.osen.cloud.service.data.coldchain;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainAlarm;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:54
 * Description: 冷链实时数据通用服务接口层
 */
public interface ColdChainAlarmService extends IService<ColdChainAlarm> {

    /**
     * 插入冷链报警历史记录数据
     *
     * @param coldChainAlarm 参数
     */
    void insertAlarm(ColdChainAlarm coldChainAlarm);

    /**
     * 根据指定用户获取用户设备实时报警数据
     *
     * @param account 账号
     * @return 信息
     */
    List<ColdChainAlarm> getRealtimeAlarm(String account);
}
