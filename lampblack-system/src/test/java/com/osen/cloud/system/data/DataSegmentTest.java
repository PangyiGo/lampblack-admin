package com.osen.cloud.system.data;

import com.osen.cloud.system.socket.utils.DataSegmentParseUtil;
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
        String data = "##0137QN=20190813155800001;ST=22;CN=2051;PW=123456;MN=2019051703100020;Flag=5;" +
                "CP=&&DataTime=20190813155800;LAMPBLACK-Rtd=0.0,LAMPBLACK-Flag=N&&9B81";

        Map<String, Object> stringObjectMap = dataSegmentParseUtil.parseDataTOMap(data);

        dataSegmentParseUtil.chooseHandlerType(stringObjectMap);
    }
}
