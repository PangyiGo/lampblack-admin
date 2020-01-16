package com.osen.cloud.service.data.coldchain;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainHour;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:54
 * Description: 冷链实时数据通用服务接口层
 */
public interface ColdChainHourService extends IService<ColdChainHour> {

    /**
     * 插入冷链小时历史数据
     *
     * @param coldChainHour 参数
     */
    void insertHour(ColdChainHour coldChainHour);

    /**
     * 创建新表
     *
     * @param tableName 新表名
     */
    void createNewTable(String tableName);

    /**
     * 根据设备号查询设备时间的历史数据记录
     *
     * @param start    开始时间
     * @param end      结束时间
     * @param deviceNo 设备号
     * @return 信息
     */
    List<ColdChainHour> queryHistoryByDate(LocalDateTime start, LocalDateTime end, String deviceNo);

    /**
     * 根据设备号查询设备时间的历史数据记录
     *
     * @param start    开始时间
     * @param end      结束时间
     * @param deviceNo 设备号
     * @param monitor  监控点
     * @return 信息
     */
    List<ColdChainHour> queryHistoryByDate(LocalDateTime start, LocalDateTime end, String deviceNo, String monitor);

    /**
     * 查询重复数据
     *
     * @param deviceNo 设备号
     * @param dateTime 时间
     * @return 信息
     */
    ColdChainHour getOneData(String deviceNo, LocalDateTime dateTime);
}
