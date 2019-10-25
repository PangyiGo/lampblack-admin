package com.osen.cloud.system.dev_data;

import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.service.data.lampblack.DataDayService;
import com.osen.cloud.service.data.lampblack.DataHistoryService;
import com.osen.cloud.service.data.lampblack.DataHourService;
import com.osen.cloud.service.data.lampblack.DataMinuteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * User: PangYi
 * Date: 2019-09-18
 * Time: 15:38
 * Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DataHistoryTest {

    @Autowired
    private DataHistoryService dataHistoryService;

    @Autowired
    private DataMinuteService dataMinuteService;

    @Autowired
    private DataHourService dataHourService;

    @Autowired
    private DataDayService dataDayService;

    @Test
    public void test01() {
        dataHistoryService.createNewTable(ConstUtil.currentTableName(ConstUtil.REALTIME_TB));
        dataMinuteService.createNewTable(ConstUtil.currentTableName(ConstUtil.MINUTE_TB));
        dataHourService.createNewTable(ConstUtil.currentTableName(ConstUtil.HOUR_TB));
        dataDayService.createNewTable(ConstUtil.currentTableName(ConstUtil.DAY_TB));
    }
}
