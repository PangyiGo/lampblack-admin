package com.osen.cloud.system.socket.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.DateTimeUtil;
import com.osen.cloud.service.device.DeviceService;
import com.osen.cloud.system.socket.service.LampblackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

/**
 * User: PangYi
 * Date: 2019-09-06
 * Time: 17:50
 * Description: HJ212协议数据解析工具
 */
@Slf4j
@Component
public class DataSegmentParseUtil {

    @Autowired
    private LampblackService lampblackService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DeviceService deviceService;

    /**
     * 将数据转换成Map数据类型
     *
     * @param dataSegment 数据段
     * @return 结果数据
     */
    public Map<String, Object> parseDataTOMap(String dataSegment) {

        String segmnet = null;
        try {
            if (StringUtils.isEmpty(dataSegment))
                return null;
            // 数据格式包尾
            dataSegment = dataSegment + "\r\n";

            // 判断是否00开头
            if (dataSegment.startsWith("00##"))
                dataSegment = StrUtil.sub(dataSegment, 2, dataSegment.length());

            // 包头校验
            if (!dataSegment.startsWith("##"))
                return null;

            //数据长度校验
            int length = Integer.valueOf(StrUtil.sub(dataSegment, 2, 6));
            segmnet = StrUtil.sub(dataSegment, 6, -6);
            if (segmnet.length() != length)
                return null;

            //CRC校验
            String crc = StrUtil.sub(dataSegment, -6, -2);
            if (!CRCValidationUtil.validateCRC(segmnet, crc))
                return null;
        } catch (Exception e) {
            log.error("数据解析异常，返回Null");
            return null;
        }
        return this.parseDataArea(segmnet);
    }

    /**
     * 数据段解析
     *
     * @param data 数据段
     * @return 信息
     */
    private Map<String, Object> parseDataArea(String data) {
        // 保存解析数据
        Map<String, Object> result = new HashMap<>();
        try {
            // 分割数据段，默认情况下一般为7
            List<String> dataSplit = StrUtil.split(data, ';', 7);
            for (String sub : dataSplit) {
                List<String> valueSplit = StrUtil.split(sub, '=', 2);
                // 数据区解析
                if (valueSplit.get(0).equals("CP")) {
                    // 数据区信息
                    Map<String, Object> area = new HashMap<>();
                    // 数据区内容
                    String dataArea = valueSplit.get(1);
                    // 格式化内容
                    dataArea = StrUtil.replace(dataArea, "&&", "");
                    dataArea = StrUtil.replace(dataArea, ",", ";");
                    // 分割数据区内容
                    String[] split = StrUtil.split(dataArea, ";");
                    for (String value : split) {
                        List<String> KeyLists = StrUtil.split(value, '=', 2);
                        //格式化日期
                        if (KeyLists.get(0).equals("DataTime")) {
                            Date dateTime = DateUtil.parse(KeyLists.get(1), ConstUtil.DATE_FORMAT);
                            LocalDateTime localDateTime = DateTimeUtil.asLocalDateTime(dateTime);
                            area.put(KeyLists.get(0), localDateTime);
                        } else {
                            area.put(KeyLists.get(0), KeyLists.get(1));
                        }
                    }
                    // 格式：{ "CP":{ } }
                    result.put(valueSplit.get(0), area);
                } else {
                    result.put(valueSplit.get(0), valueSplit.get(1));
                }
            }
        } catch (Exception e) {
            log.error("数据段解析异常");
            return null;
        }
        return result;
    }

    /**
     * 油烟设备数据封装
     *
     * @param parseData map数据
     */
    @Async
    public void chooseHandlerType(Map<String, Object> parseData, String connectionID) {
        try {
            Integer CN = Integer.valueOf((String) parseData.get("CN"));
            switch (CN) {
                // 实时数据类型
                case 2011:
                    lampblackService.handleRealTimeData(parseData, connectionID);
                    break;
                // 分钟数据类型
                case 2051:
                    lampblackService.handleMinuteData(parseData, connectionID);
                    break;
                // 小时数据类型
                case 2061:
                    lampblackService.handleHourData(parseData, connectionID);
                    break;
                // 天数据类型
                case 2031:
                    lampblackService.handleDayData(parseData, connectionID);
                    break;
                default:
                    log.warn("no match type handler");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            log.info("数据存储异常，上传格式是否不一致");
        }
    }

    /**
     * 设备断开连接处理
     *
     * @param connectionId 连接ID
     */
    @Async
    public void disConnectionDevice(String connectionId) {
        String deviceNo = (String) stringRedisTemplate.boundHashOps(ConstUtil.DEVICE_KEY).get(connectionId);
        stringRedisTemplate.boundHashOps(ConstUtil.DEVICE_KEY).delete(connectionId);
        if (StringUtils.isNotEmpty(deviceNo)) {
            deviceService.updateDeviceStatus(ConstUtil.CLOSE_STATUS, deviceNo);
        }
    }
}
