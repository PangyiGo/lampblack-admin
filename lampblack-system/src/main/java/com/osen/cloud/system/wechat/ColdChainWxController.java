package com.osen.cloud.system.wechat;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainAlarm;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainHistory;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainMonitor;
import com.osen.cloud.common.entity.system_device.Device;
import com.osen.cloud.common.enums.DeviceType;
import com.osen.cloud.common.enums.MonthCode;
import com.osen.cloud.common.result.RestResult;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.RestResultUtil;
import com.osen.cloud.common.utils.TableUtil;
import com.osen.cloud.service.data.coldchain.ColdChainHistoryService;
import com.osen.cloud.service.data.coldchain.ColdChainMonitorService;
import com.osen.cloud.service.device.DeviceService;
import com.osen.cloud.system.config.db_config.MybatisPlusConfig;
import com.osen.cloud.system.dev_data.coldchain.util.AddressVO;
import com.osen.cloud.system.dev_data.coldchain.util.DataVO;
import com.osen.cloud.system.dev_data.coldchain.util.RealTimeVO;
import com.osen.cloud.system.wechat.model.ColdChainMapModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2020-01-16
 * Time: 15:02
 * Description:
 */
@RestController
@RequestMapping("${restful.prefix}")
@Slf4j
public class ColdChainWxController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ColdChainHistoryService coldChainHistoryService;

    @Autowired
    private ColdChainMonitorService coldChainMonitorService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取当前用户的VOC全部设备列表
     *
     * @return 信息
     */
    @PostMapping("/wechat/coldchain/devices")
    public RestResult getColdchainDeviceToUsername() {
        List<Device> deviceList = deviceService.findAllToMap(DeviceType.ColdChain.getName());
        return RestResultUtil.success(deviceList);
    }

    /**
     * 根据设备号获取对应监控点名称
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/wechat/coldchain/monitor/{deviceNo}")
    public RestResult getColdchainMonitor(@PathVariable("deviceNo") String deviceNo) {
        ColdChainMonitor monitor = coldChainMonitorService.getMonitorToDeviceNo(deviceNo);
        if (monitor == null)
            return RestResultUtil.success(new ColdChainMonitor());
        return RestResultUtil.success(monitor);
    }

    /**
     * 获取指定设备号的最新实时数据
     *
     * @param deviceNo 设备号
     * @return 信息
     */
    @PostMapping("/wechat/coldchain/rt/{deviceNo}")
    public RestResult getColdchainRealtime(@PathVariable("deviceNo") String deviceNo) {
        ColdChainHistory realtime = coldChainHistoryService.getRealtime(deviceNo);
        ColdChainMonitor monitor = coldChainMonitorService.getMonitorToDeviceNo(deviceNo);
        RealTimeVO realTimeVO = new RealTimeVO();
        if (realtime != null)
            BeanUtil.copyProperties(realtime, realTimeVO);
        if (monitor != null) {
            BeanUtil.copyProperties(monitor, realTimeVO);
        } else {
            realTimeVO.setM01("未定义#1");
            realTimeVO.setM02("未定义#2");
            realTimeVO.setM03("未定义#3");
            realTimeVO.setM03("未定义#4");
        }
        return RestResultUtil.success(realTimeVO);
    }

    /**
     * 获取当前用户的实时数据标记地图
     *
     * @return 信息
     */
    @PostMapping("/wechat/coldchain/maps")
    public RestResult getColdchainMap() {
        List<ColdChainMapModel> coldChainMapModelList = new ArrayList<>(0);
        // 设备列表
        List<Device> deviceList = deviceService.findAllToMap(DeviceType.ColdChain.getName());
        for (Device device : deviceList) {
            ColdChainMapModel mapModel = new ColdChainMapModel();
            // 设备信息
            BeanUtil.copyProperties(device, mapModel);
            // 实时数据
            ColdChainHistory realtime = coldChainHistoryService.getRealtime(device.getDeviceNo());
            ColdChainMonitor monitor = coldChainMonitorService.getMonitorToDeviceNo(device.getDeviceNo());
            RealTimeVO realTimeVO = new RealTimeVO();
            if (realtime != null)
                BeanUtil.copyProperties(realtime, realTimeVO);
            if (monitor != null) {
                BeanUtil.copyProperties(monitor, realTimeVO);
            } else {
                realTimeVO.setM01("未定义#1");
                realTimeVO.setM02("未定义#2");
                realTimeVO.setM03("未定义#3");
                realTimeVO.setM03("未定义#4");
            }
            BeanUtil.copyProperties(realTimeVO, mapModel);
            // 添加
            coldChainMapModelList.add(mapModel);
        }
        return RestResultUtil.success(coldChainMapModelList);
    }

    /**
     * 获取设备历史数据
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/wechat/coldchain/history/{monitor}")
    public RestResult queryHistoryByDate(@RequestBody Map<String, Object> params, @PathVariable("monitor") String monitor) {
        // 参数
        String deviceNo = (String) params.get("deviceNo");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 数据获取
        List<ColdChainHistory> coldChainHistories = new ArrayList<>(0);
        // 时间日期格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.DATE);
        // 开始时间
        LocalDate startDates = LocalDate.parse(startTime, dateTimeFormatter);
        LocalDateTime startDate = LocalDateTime.of(startDates.getYear(), startDates.getMonthValue(), startDates.getDayOfMonth(), 0, 0, 0);
        // 结束时间
        LocalDate endDates = LocalDate.parse(endTime, dateTimeFormatter);
        LocalDateTime endDate = LocalDateTime.of(endDates.getYear(), endDates.getMonthValue(), endDates.getDayOfMonth(), 23, 59, 59);
        // 构建数据表
        List<String> queryTableName = ConstUtil.queryTableName(startDate, endDate, TableUtil.ColdHistory);
        for (String tableName : queryTableName) {
            if (ConstUtil.compareToTime(MonthCode.ColdChain.getMonth()))
                continue;
            MybatisPlusConfig.TableName.set(tableName);
            List<ColdChainHistory> chainHistories = coldChainHistoryService.queryHistoryByDate(startDate, endDate, deviceNo, monitor);
            coldChainHistories.addAll(chainHistories);
        }
        List<DataVO> dataVOS = new ArrayList<>(0);
        for (ColdChainHistory coldChainHistory : coldChainHistories) {
            DataVO dataVO = new DataVO();
            switch (monitor) {
                case "m01":
                    dataVO.setTemp(coldChainHistory.getT01());
                    dataVO.setDamp(coldChainHistory.getH01());
                    dataVO.setDateTime(coldChainHistory.getDateTime());
                    break;
                case "m02":
                    dataVO.setTemp(coldChainHistory.getT02());
                    dataVO.setDamp(coldChainHistory.getH02());
                    dataVO.setDateTime(coldChainHistory.getDateTime());
                    break;
                case "m03":
                    dataVO.setTemp(coldChainHistory.getT03());
                    dataVO.setDamp(coldChainHistory.getH03());
                    dataVO.setDateTime(coldChainHistory.getDateTime());
                    break;
                case "m04":
                    dataVO.setTemp(coldChainHistory.getT04());
                    dataVO.setDamp(coldChainHistory.getH04());
                    dataVO.setDateTime(coldChainHistory.getDateTime());
                    break;
            }
            dataVOS.add(dataVO);
        }
        return RestResultUtil.success(dataVOS);
    }

    /**
     * 获取设备列表实时报警数据
     *
     * @return 信息
     */
    @PostMapping("/wechat/coldchain/alarm")
    public RestResult getColdchainAlarm() {
        // 数据表
        List<ColdChainMapModel> coldChainMapModelList = new ArrayList<>(0);
        // 获取用户设备列表
        List<Device> deviceList = deviceService.findAllToMap(DeviceType.ColdChain.getName());
        // 获取报警数据
        BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps(TableUtil.Cold_Alarm);
        for (Device device : deviceList) {
            // 是否存在报警
            Boolean hasKey = operations.hasKey(device.getDeviceNo());
            if (hasKey) {
                ColdChainMapModel mapModel = new ColdChainMapModel();
                // 设备号信息
                BeanUtil.copyProperties(device, mapModel);
                // 报警值
                String json = (String) operations.get(device.getDeviceNo());
                ColdChainAlarm coldChainAlarm = JSON.parseObject(json, ColdChainAlarm.class);
                BeanUtil.copyProperties(coldChainAlarm, mapModel);
                // 监控点
                ColdChainMonitor monitor = coldChainMonitorService.getMonitorToDeviceNo(device.getDeviceNo());
                if (monitor != null) {
                    BeanUtil.copyProperties(monitor, mapModel);
                } else {
                    mapModel.setM01("未定义#1");
                    mapModel.setM02("未定义#2");
                    mapModel.setM03("未定义#3");
                    mapModel.setM03("未定义#4");
                }
                // 添加
                coldChainMapModelList.add(mapModel);
            }
        }
        return RestResultUtil.success(coldChainMapModelList);
    }

    /**
     * 查询冷链设备历史轨迹
     *
     * @param params 参数
     * @return 信息
     */
    @PostMapping("/wechat/coldchain/locus")
    public RestResult getLocus(@RequestBody Map<String, Object> params) {
        // 参数
        String deviceNo = (String) params.get("deviceNo");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        // 数据获取
        List<ColdChainHistory> coldChainHistories = new ArrayList<>(0);
        // 时间日期格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstUtil.DATE);
        // 开始时间
        LocalDate startDates = LocalDate.parse(startTime, dateTimeFormatter);
        LocalDateTime startDate = LocalDateTime.of(startDates.getYear(), startDates.getMonthValue(), startDates.getDayOfMonth(), 0, 0, 0);
        // 结束时间
        LocalDate endDates = LocalDate.parse(endTime, dateTimeFormatter);
        LocalDateTime endDate = LocalDateTime.of(endDates.getYear(), endDates.getMonthValue(), endDates.getDayOfMonth(), 23, 59, 59);
        // 构建数据表
        List<String> queryTableName = ConstUtil.queryTableName(startDate, endDate, TableUtil.ColdHistory);
        for (String tableName : queryTableName) {
            if (ConstUtil.compareToTime( MonthCode.ColdChain.getMonth()))
                continue;
            MybatisPlusConfig.TableName.set(tableName);
            List<ColdChainHistory> locusToUser = coldChainHistoryService.getLocusToUser(startDate, endDate, deviceNo);
            coldChainHistories.addAll(locusToUser);
        }
        // 返回结果
        List<AddressVO> addressVOS = new ArrayList<>(0);
        for (ColdChainHistory coldChainHistory : coldChainHistories) {
            if (coldChainHistory == null)
                continue;
            AddressVO addressVO = new AddressVO();
            BeanUtil.copyProperties(coldChainHistory, addressVO);
            addressVOS.add(addressVO);
        }
        return RestResultUtil.success(addressVOS);
    }
}
