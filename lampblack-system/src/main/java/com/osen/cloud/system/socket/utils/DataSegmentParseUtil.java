package com.osen.cloud.system.socket.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-09-06
 * Time: 17:50
 * Description: HJ212协议数据解析工具
 */
public class DataSegmentParseUtil {

    // 实时数据格式
    private String[] realTimeSensorFlag = {"-Rtd", "-Flag"};

    // 分钟，小时，天数据格式
    private String[] othersSensorFlag = {"-Avg", "-Max", "-Min", "-Flag"};

    /**
     * 将数据转换成Map数据类型
     *
     * @param dataSegment 数据段
     * @return 结果数据
     */
    public static Map<String, Object> parseDataTOMap(String dataSegment) {

        if (StringUtils.isEmpty(dataSegment))
            return null;
        // 数据格式包尾
        dataSegment = dataSegment + "\r\n";

        // 包头校验
        if (!dataSegment.startsWith("##"))
            return null;

        //数据长度校验
        int length = Integer.valueOf(StrUtil.sub(dataSegment, 2, 6));
        String segmnet = StrUtil.sub(dataSegment, 6, -6);
        if (segmnet.length() != length)
            return null;

        //CRC校验
        String crc = StrUtil.sub(dataSegment, -6, -2);
        if (!HJ212ValidationUtil.validateCRC(segmnet, crc))
            return null;

        return DataSegmentParseUtil.parseDataArea(segmnet);
    }

    /**
     * 数据段解析
     *
     * @param data 数据段
     * @return 信息
     */
    private static Map<String, Object> parseDataArea(String data) {
        // 保存解析数据
        Map<String, Object> result = new HashMap<>();
        try {
            // 分割数据段
            List<String> dataSplit = StrUtil.split(data, ';', 7);
            for (String sub : dataSplit) {
                List<String> valueSplit = StrUtil.split(sub, '=', 2);
                // 数据区解析
                if (valueSplit.get(0).equals("CP")) {
                    //数据区信息
                    Map<String, Object> area = new HashMap<>();
                    String dataArea = valueSplit.get(1);
                    dataArea = StrUtil.replace(dataArea, "&&", "");
                    dataArea = StrUtil.replace(dataArea, ",", ";");
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
                    result.put(valueSplit.get(0), area);
                } else {
                    result.put(valueSplit.get(0), valueSplit.get(1));
                }
            }
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    public static void main(String[] args) {
        String data = "##0275QN=20190814170100001;ST=22;CN=2011;PW=123456;MN=2019031801100018;Flag=5;CP=&&DataTime=20190814170100;a01007-Rtd=1.7,a01007-Flag=N;a01008-Rtd=199.1,a01008-Flag=N;a01001-Rtd=22.9,a01001-Flag=N;a01002-Rtd=85.8,a01002-Flag=N;a01006-Rtd=98.7,a01006-Flag=N;R01-Rtd=0.0,R01-Flag=N&&9301";

        Map<String, Object> stringObjectMap = parseDataTOMap(data);

        System.out.println(stringObjectMap);
    }
}
