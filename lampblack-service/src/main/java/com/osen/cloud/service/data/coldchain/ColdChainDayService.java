package com.osen.cloud.service.data.coldchain;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainDay;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-10-24
 * Time: 14:54
 * Description: 冷链实时数据通用服务接口层
 */
public interface ColdChainDayService extends IService<ColdChainDay> {

    /**
     * 插入冷链天历史数据
     *
     * @param coldChainDay 参数
     */
    void insertDay(ColdChainDay coldChainDay);

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
    List<ColdChainDay> queryHistoryByDate(LocalDateTime start, LocalDateTime end, String deviceNo);
}
