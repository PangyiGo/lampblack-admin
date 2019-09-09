package com.osen.cloud.service.data;

import com.baomidou.mybatisplus.extension.service.IService;
import com.osen.cloud.common.entity.DataHour;

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
}
