package com.osen.cloud.system.dev_data;

import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.common.utils.TableUtil;
import com.osen.cloud.service.data.coldchain.ColdChainDayService;
import com.osen.cloud.service.data.coldchain.ColdChainHistoryService;
import com.osen.cloud.service.data.coldchain.ColdChainHourService;
import com.osen.cloud.service.data.coldchain.ColdChainMinuteService;
import com.osen.cloud.service.data.lampblack.DataDayService;
import com.osen.cloud.service.data.lampblack.DataHistoryService;
import com.osen.cloud.service.data.lampblack.DataHourService;
import com.osen.cloud.service.data.lampblack.DataMinuteService;
import com.osen.cloud.service.data.vocs.VocDayService;
import com.osen.cloud.service.data.vocs.VocHistoryService;
import com.osen.cloud.service.data.vocs.VocHourService;
import com.osen.cloud.service.data.vocs.VocMinuteService;
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

    @Autowired
    private VocHistoryService vocHistoryService;

    @Autowired
    private VocMinuteService vocMinuteService;

    @Autowired
    private VocHourService vocHourService;

    @Autowired
    private VocDayService vocDayService;

    @Autowired
    private ColdChainHistoryService coldChainHistoryService;

    @Autowired
    private ColdChainMinuteService coldChainMinuteService;

    @Autowired
    private ColdChainHourService coldChainHourService;

    @Autowired
    private ColdChainDayService coldChainDayService;

    @Test
    public void test01() {
        dataHistoryService.createNewTable(ConstUtil.currentTableName(ConstUtil.REALTIME_TB));
        dataMinuteService.createNewTable(ConstUtil.currentTableName(ConstUtil.MINUTE_TB));
        dataHourService.createNewTable(ConstUtil.currentTableName(ConstUtil.HOUR_TB));
        dataDayService.createNewTable(ConstUtil.currentTableName(ConstUtil.DAY_TB));
    }

    @Test
    public void test02() {
        vocHistoryService.createNewTable(ConstUtil.createNextTableName(TableUtil.VocHistory));
        vocMinuteService.createNewTable(ConstUtil.createNextTableName(TableUtil.VocMinute));
        vocHourService.createNewTable(ConstUtil.createNextTableName(TableUtil.VocHour));
        vocDayService.createNewTable(ConstUtil.createNextTableName(TableUtil.VocDay));
    }

    @Test
    public void test03() {
        coldChainHistoryService.createNewTable(ConstUtil.createNextTableName(TableUtil.ColdHistory));
        coldChainMinuteService.createNewTable(ConstUtil.createNextTableName(TableUtil.ColdMinute));
        coldChainHourService.createNewTable(ConstUtil.createNextTableName(TableUtil.ColdHour));
        coldChainDayService.createNewTable(ConstUtil.createNextTableName(TableUtil.ColdDay));
    }
}
