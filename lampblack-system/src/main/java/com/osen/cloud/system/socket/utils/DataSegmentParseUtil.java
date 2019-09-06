package com.osen.cloud.system.socket.utils;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

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

        // 保存解析数据
        Map<String, Object> result = new HashMap<>();

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
        if (!HJ212ValidationUtil.validateCRC(dataSegment, crc))
            return null;

        return result;
    }

    public static void main(String[] args) {
        String data = "##0137QN=20190813155800001;ST=22;CN=2011;PW=123456;MN=2019051703100020;Flag=5;CP=&&DataTime=20190813155800;LAMPBLACK-Rtd=0.0,LAMPBLACK-Flag=N&&5880\r\n";

        String sub1 = StrUtil.sub(data, 6, -6);
        List<String> split = StrUtil.split(sub1, ';', 7);

        //待续。。。。。
    }
}
