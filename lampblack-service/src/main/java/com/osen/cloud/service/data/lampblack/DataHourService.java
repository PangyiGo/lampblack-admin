package com.osen.cloud.service.data.lampblack;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.dev_lampblack.DataHour;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 11:25
 * Description: 设备小数上传数据服务接口
 */
public interface DataHourService extends IService<DataHour> {

    /**
     * 插入设备小时上传数据
     *
     * @param dataHour 数据
     */
    void insertHourData(DataHour dataHour);

    /**
     * 创建新表
     *
     * @param tableName 新表名
     */
    void createNewTable(String tableName);

    /**
     * 查询小时数据历史记录
     *
     * @param start    开始时间
     * @param end      结束时间
     * @param deviceNo 设备号
     * @param type 1表示历史查询，2表示数据导出
     * @return 信息
     */
    List<DataHour> queryDataHourByDate(LocalDateTime start, LocalDateTime end, String deviceNo,int type);

    /**
     * 查询重复数据
     *
     * @param deviceNo 设备号
     * @param dateTime 时间
     * @return 信息
     */
    DataHour getOneData(String deviceNo, LocalDateTime dateTime);
}
