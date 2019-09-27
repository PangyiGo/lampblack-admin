package com.osen.cloud.system.data.controller;

import com.osen.cloud.common.entity.DataMinute;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.service.data.DataMinuteService;
import com.osen.cloud.system.db_config.MybatisPlusConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-09-27
 * Time: 16:14
 * Description: 设备分钟数据控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class DataMinuteController {

    @Autowired
    private DataMinuteService dataMinuteService;

    /**
     * 查询分钟数据历史记录
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/data/minute/queryByDate")
    public RestResult queryDataMinuteByDate(@RequestBody Map<String, Object> params) {
        List<DataMinute> dataMinutes = new ArrayList<>(0);
        // 参数
        String deviceNo = (String) params.get("deviceNo");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 时间日期格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.QUERY_DATE);
        // 开始时间
        LocalDateTime startDate = LocalDateTime.parse(startTime, dateTimeFormatter);
        // 结束时间
        LocalDateTime endDate = LocalDateTime.parse(endTime, dateTimeFormatter);
        // 构建数据表
        List<String> queryTableName = ConstUtil.queryTableName(startDate, startDate, ConstUtil.MINUTE_TB);
        for (String tableName : queryTableName) {
            MybatisPlusConfig.TableName.set(tableName);
            List<DataMinute> history = dataMinuteService.queryDataMinuteByDate(startDate, endDate, deviceNo);
            dataMinutes.addAll(history);
        }
        return RestResultUtil.success(dataMinutes);
    }
}
