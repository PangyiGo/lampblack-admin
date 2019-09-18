package com.osen.cloud.system.db_config;

import lombok.extern.slf4j.Slf4j;
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

    /**
     * 每月份最后一天执行
     */
    @Scheduled(cron = "0 0 0 1 * ? ")
    public void taskCreateTable() {
        log.info("当前时间：" + LocalDateTime.now() + ": 定时创建当前下一月份数据表");
    }
}
