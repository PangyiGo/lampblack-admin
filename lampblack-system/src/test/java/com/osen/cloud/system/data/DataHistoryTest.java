package com.osen.cloud.system.data;

import com.osen.cloud.service.data.DataHistoryService;
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

    @Test
    public void test01() {
        dataHistoryService.createNewTable("gag");
    }
}
