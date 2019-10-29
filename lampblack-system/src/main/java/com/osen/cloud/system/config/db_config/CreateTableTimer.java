package com.osen.cloud.system.config.db_config;

import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.TableUtil;
import com.osen.cloud.service.data.lampblack.DataDayService;
import com.osen.cloud.service.data.lampblack.DataHistoryService;
import com.osen.cloud.service.data.lampblack.DataHourService;
import com.osen.cloud.service.data.lampblack.DataMinuteService;
import com.osen.cloud.service.data.vocs.VocDayService;
import com.osen.cloud.service.data.vocs.VocHistoryService;
import com.osen.cloud.service.data.vocs.VocHourService;
import com.osen.cloud.service.data.vocs.VocMinuteService;
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

    @Autowired
    private VocHistoryService vocHistoryService;

    @Autowired
    private VocMinuteService vocMinuteService;

    @Autowired
    private VocHourService vocHourService;

    @Autowired
    private VocDayService vocDayService;

    /**
     * 每月1号的零点10秒执行
     */
    @Scheduled(cron = "10 0 0 1 * ? ")
    public void taskCreateTable() {
        log.info("当前时间：" + LocalDateTime.now() + ": 定时创建油烟设备当前下一月份数据表");

        dataHistoryService.createNewTable(ConstUtil.createNextTableName(ConstUtil.REALTIME_TB));

        dataMinuteService.createNewTable(ConstUtil.createNextTableName(ConstUtil.MINUTE_TB));

        dataHourService.createNewTable(ConstUtil.createNextTableName(ConstUtil.HOUR_TB));

        dataDayService.createNewTable(ConstUtil.createNextTableName(ConstUtil.DAY_TB));
    }

    /**
     * 每月1号的零点10秒执行
     */
    @Scheduled(cron = "15 0 0 1 * ? ")
    public void vocTask() {
        log.info("当前时间：" + LocalDateTime.now() + ": 定时创建VOC设备当前下一月份数据表");

        vocHistoryService.createNewTable(ConstUtil.createNextTableName(TableUtil.VocHistory));

        vocMinuteService.createNewTable(ConstUtil.createNextTableName(TableUtil.VocMinute));

        vocHourService.createNewTable(ConstUtil.createNextTableName(TableUtil.VocHour));

        vocDayService.createNewTable(ConstUtil.createNextTableName(TableUtil.VocDay));
    }

    /**
     * 定时每天凌晨删除一次无效的connectionID
     */
    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void deleteDeviceNo() {
        stringRedisTemplate.delete(ConstUtil.DEVICE_KEY);
    }
}
