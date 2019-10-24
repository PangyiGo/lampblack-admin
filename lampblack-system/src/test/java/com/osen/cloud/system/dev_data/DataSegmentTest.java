package com.osen.cloud.system.dev_data;

import com.osen.cloud.system.system_socket.utils.DataSegmentParseUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-09-09
 * Time: 10:20
 * Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DataSegmentTest {

    @Autowired
    private DataSegmentParseUtil dataSegmentParseUtil;

    @Test
    public void test01() {
        String data = "##0277QN=20190813155800001;ST=22;CN=2031;PW=123456;MN=2019051703100020;Flag=5;" + "CP=&&DataTime=20190813155800;LAMPBLACK-Avg=6.23,LAMPBLACK-Max=9.66,LAMPBLACK-Min=5.11,LAMPBLACK-Flag=N;PM-Avg=6.23,PM-Max=9.66,PM-Min=5.11,PM-Flag=N;NMHC-Avg=4.23,NMHC-Max=10.66,NMHC-Min=3.11,NMHC-Flag=N&&8BC1";

        Map<String, Object> stringObjectMap = dataSegmentParseUtil.parseDataTOMap(data);

        dataSegmentParseUtil.chooseHandlerType(stringObjectMap, "");
    }
}
