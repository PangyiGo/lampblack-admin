package com.osen.cloud.service.data;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.dev_lampblack.DataMinute;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 11:25
 * Description: 设备分钟上传数据服务接口
 */
public interface DataMinuteService extends IService<DataMinute> {

    /**
     * 插入设备分钟上传数据
     *
     * @param dataMinute 数据
     */
    void insertMinuteData(DataMinute dataMinute);

    /**
     * 创建新表
     *
     * @param tableName 新表名
     */
    void createNewTable(String tableName);

    /**
     * 查询分钟数据历史记录
     *
     * @param start    开始时间
     * @param end      结束时间
     * @param deviceNo 设备号
     * @return 信息
     */
    List<DataMinute> queryDataMinuteByDate(LocalDateTime start, LocalDateTime end, String deviceNo);
}
