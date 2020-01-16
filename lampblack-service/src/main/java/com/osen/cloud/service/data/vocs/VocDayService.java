package com.osen.cloud.service.data.vocs;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.dev_vocs.VocDay;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:22
 * Description: VOC设备实时数据通用接口层
 */
public interface VocDayService extends IService<VocDay> {

    /**
     * 插入天数历史数据
     *
     * @param vocDay 参数
     */
    void insertDay(VocDay vocDay);

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
    List<VocDay> queryHistoryByDate(LocalDateTime start, LocalDateTime end, String deviceNo);

    /**
     * 查询重复数据
     *
     * @param deviceNo 设备号
     * @param dateTime 时间
     * @return 信息
     */
    VocDay getOneData(String deviceNo, LocalDateTime dateTime);
}
