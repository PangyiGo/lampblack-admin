package com.osen.cloud.system.dev_data.vocs.controller;

import com.osen.cloud.common.entity.dev_vocs.VocHistory;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.service.data.vocs.VocHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-10-31
 * Time: 15:36
 * Description: VOC设备通用控制器
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class VocController {

    @Autowired
    private VocHistoryService vocHistoryService;

    /**
     * 获取指定设备号的最新实时数据
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/voc/realtime/{deviceNo}")
    public RestResult getRealtime(@PathVariable("deviceNo") String deviceNo) {
        VocHistory realtime = new VocHistory();
        if (realtime != null)
            realtime = vocHistoryService.getRealtime(deviceNo);
        return RestResultUtil.success(realtime);
    }

    /**
     * 获取指定设备号的参数的历史数据
     *
     * @param args     参数
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/voc/today/{args}/{deviceNo}")
    public RestResult getDataToday(@PathVariable("args") String args, @PathVariable("deviceNo") String deviceNo) {
        List<VocHistory> histories = vocHistoryService.getDataToday(args, deviceNo);
        return RestResultUtil.success(histories);
    }
}
