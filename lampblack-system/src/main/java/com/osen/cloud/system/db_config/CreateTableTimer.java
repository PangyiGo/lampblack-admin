package com.osen.cloud.system.db_config;

import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.service.data.DataDayService;
import com.osen.cloud.service.data.DataHistoryService;
import com.osen.cloud.service.data.DataHourService;
import com.osen.cloud.service.data.DataMinuteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-09-18
 * Time: 16:30
 * Description: 定时创建数据表
 */
@Component
@Slf4j
public class CreateTableTimer {

    @Autowired
    private DataHistoryService dataHistoryService;

    @Autowired
    private DataMinuteService dataMinuteService;

    @Autowired
    private DataHourService dataHourService;

    @Autowired
    private DataDayService dataDayService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 每月1号的零点10秒执行
     */
    @Scheduled(cron = "10 0 0 1 * ? ")
    public void taskCreateTable() {
        log.info("当前时间：" + LocalDateTime.now() + ": 定时创建当前下一月份数据表");

        dataHistoryService.createNewTable(ConstUtil.createNextTableName(ConstUtil.REALTIME_TB));

        dataMinuteService.createNewTable(ConstUtil.createNextTableName(ConstUtil.MINUTE_TB));

        dataHourService.createNewTable(ConstUtil.createNextTableName(ConstUtil.HOUR_TB));

        dataDayService.createNewTable(ConstUtil.createNextTableName(ConstUtil.DAY_TB));
    }

    /**
     * 定时每天凌晨删除一次无效的connectionID
     */
    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void deleteDeviceNo() {
        stringRedisTemplate.delete(ConstUtil.DEVICE_KEY);
    }
}
