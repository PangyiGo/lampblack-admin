package com.osen.cloud.service.data.vocs;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.dev_vocs.VocMinute;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:22
 * Description: VOC设备实时数据通用接口层
 */
public interface VocMinuteService extends IService<VocMinute> {

    /**
     * 插入VOC分钟历史数据
     *
     * @param vocMinute 参数
     */
    void insertMinute(VocMinute vocMinute);

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
    List<VocMinute> queryHistoryByDate(LocalDateTime start, LocalDateTime end, String deviceNo);
}
