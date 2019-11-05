package com.osen.cloud.service.data.coldchain;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainMonitor;

import java.util.List;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:54
 * Description: 冷链实时数据通用服务接口层
 */
public interface ColdChainMonitorService extends IService<ColdChainMonitor> {

    /**
     * 插入冷链监控点
     *
     * @param coldChainMonitor 参数
     */
    void insertColdMonitor(ColdChainMonitor coldChainMonitor);

    /**
     * 插入默认监控点名称
     *
     * @param deviceNo 设备号
     */
    void insertDefaultMonitor(String deviceNo);

    /**
     * 根据设备号获取对应的监控点名称
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    ColdChainMonitor getMonitorToDeviceNo(String deviceNo);

    /**
     * 设置监控点更新
     *
     * @param coldChainMonitor 参数
     * @return 信息
     */
    boolean updateMonitor(ColdChainMonitor coldChainMonitor);

    /**
     * 获取当前用户的冷链设备监控点
     *
     * @param deviceNos 设备号列表
     * @return 信息
     */
    Map<String, Object> getAllMonitorToUser(List<String> deviceNos, Map<String, Object> params);
}
