package com.osen.cloud.system.dev_data;

import com.alibaba.fastjson.JSON;
import com.osen.cloud.common.entity.dev_coldchain.ColdChainHistory;
import com.osen.cloud.common.utils.TableUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User: PangYi
 * Date: 2019-10-31
 * Time: 11:58
 * Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ColdChainTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test01() {
        ColdChainHistory coldChainHistory = new ColdChainHistory();
        coldChainHistory.setId(1);
        coldChainHistory.setDeviceNo("2019102900001");
        coldChainHistory.setDateTime(LocalDateTime.now());

        coldChainHistory.setT01(new BigDecimal(20));
        coldChainHistory.setH01(new BigDecimal(30));
        coldChainHistory.setT01Flag("N");
        coldChainHistory.setH01Flag("N");

        coldChainHistory.setT02(new BigDecimal(26));
        coldChainHistory.setH02(new BigDecimal(22));
        coldChainHistory.setT02Flag("N");
        coldChainHistory.setH02Flag("N");

        coldChainHistory.setT03(new BigDecimal(10));
        coldChainHistory.setH03(new BigDecimal(30));
        coldChainHistory.setT03Flag("N");
        coldChainHistory.setH03Flag("N");

        coldChainHistory.setT04(new BigDecimal(40));
        coldChainHistory.setH04(new BigDecimal(20));
        coldChainHistory.setT04Flag("N");
        coldChainHistory.setH04Flag("N");

        stringRedisTemplate.boundHashOps(TableUtil.Cold_RealTime).put(coldChainHistory.getDeviceNo(), JSON.toJSONString(coldChainHistory));
    }

    @Test
    public void test02() {
        ColdChainHistory coldChainHistory = new ColdChainHistory();
        coldChainHistory.setId(1);
        coldChainHistory.setDeviceNo("2019102900001");
        coldChainHistory.setDateTime(LocalDateTime.now());

        coldChainHistory.setT01(new BigDecimal(20));
        coldChainHistory.setH01(new BigDecimal(30));
        coldChainHistory.setT01Flag("N");
        coldChainHistory.setH01Flag("T");

        coldChainHistory.setT02(new BigDecimal(26));
        coldChainHistory.setH02(new BigDecimal(22));
        coldChainHistory.setT02Flag("N");
        coldChainHistory.setH02Flag("N");

        coldChainHistory.setT03(new BigDecimal(10));
        coldChainHistory.setH03(new BigDecimal(30));
        coldChainHistory.setT03Flag("T");
        coldChainHistory.setH03Flag("N");

        coldChainHistory.setT04(new BigDecimal(40));
        coldChainHistory.setH04(new BigDecimal(20));
        coldChainHistory.setT04Flag("N");
        coldChainHistory.setH04Flag("N");

        stringRedisTemplate.boundHashOps(TableUtil.Cold_RealTime).put(coldChainHistory.getDeviceNo(),
                JSON.toJSONString(coldChainHistory));
    }
}
