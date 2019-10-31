package com.osen.cloud.system.dev_data.coldchain.controller;

import cn.hutool.core.bean.BeanUtil;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainHistory;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainMonitor;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.common.utils.TableUtil;
import com.osen.cloud.service.data.coldchain.ColdChainHistoryService;
import com.osen.cloud.service.data.coldchain.ColdChainMonitorService;
import com.osen.cloud.system.config.db_config.MybatisPlusConfig;
import com.osen.cloud.system.dev_data.coldchain.util.RealTimeVO;
import com.osen.cloud.system.dev_data.coldchain.util.TodayVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * User: PangYi
 * Date: 2019-10-31
 * Time: 11:07
 * Description: 冷链设备通用控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class ColdChainController {

    @Autowired
    private ColdChainHistoryService coldChainHistoryService;

    @Autowired
    private ColdChainMonitorService coldChainMonitorService;

    /**
     * 获取指定设备号的最新实时数据
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/coldchain/realtime/{deviceNo}")
    public RestResult getRealtime(@PathVariable("deviceNo") String deviceNo) {
        ColdChainHistory realtime = coldChainHistoryService.getRealtime(deviceNo);
        ColdChainMonitor monitor = coldChainMonitorService.getMonitorToDeviceNo(deviceNo);
        RealTimeVO realTimeVO = new RealTimeVO();
        if (realtime != null)
            BeanUtil.copyProperties(realtime, realTimeVO);
        if (monitor != null) {
            BeanUtil.copyProperties(monitor, realTimeVO);
        } else {
            realTimeVO.setM01("未定义监控点#1");
            realTimeVO.setM02("未定义监控点#2");
            realTimeVO.setM03("未定义监控点#3");
            realTimeVO.setM03("未定义监控点#4");
        }
        return RestResultUtil.success(realTimeVO);
    }

    /**
     * 获取指定设备号的当天实施历史数据
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/coldchain/today/{deviceNo}")
    public RestResult getDataToday(@PathVariable("deviceNo") String deviceNo) {
        // 设置表名
        MybatisPlusConfig.TableName.set(ConstUtil.currentTableName(TableUtil.ColdHistory));
        List<ColdChainHistory> chainHistories = coldChainHistoryService.getDataToday(deviceNo);
        List<TodayVO> todayVOS = new ArrayList<>(0);
        for (ColdChainHistory chainHistory : chainHistories) {
            TodayVO todayVO = new TodayVO();
            BeanUtil.copyProperties(chainHistory, todayVO);
            todayVOS.add(todayVO);
        }
        List<Object> data = new ArrayList<>(0);
        ColdChainMonitor monitor = coldChainMonitorService.getMonitorToDeviceNo(deviceNo);
        if (monitor == null) {
            monitor.setM01("未定义监控点#1");
            monitor.setM02("未定义监控点#2");
            monitor.setM03("未定义监控点#3");
            monitor.setM03("未定义监控点#4");
        }
        data.add(monitor.getM01());
        data.add(monitor.getM02());
        data.add(monitor.getM03());
        data.add(monitor.getM04());
        // 历史数据
        data.add(todayVOS);
        return RestResultUtil.success(data);
    }
}
