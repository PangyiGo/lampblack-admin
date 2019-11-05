package com.osen.cloud.system.dev_data;

import com.alibaba.fastjson.JSON;
import com.osen.cloud.common.entity.dev_vocs.VocHistory;
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
public class VocTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test01() {

        VocHistory vocHistory = new VocHistory();

        vocHistory.setDeviceNo("2019103000001");
        vocHistory.setDateTime(LocalDateTime.now());

        vocHistory.setVoc(new BigDecimal(5.6));
        vocHistory.setFlow(new BigDecimal(60));
        vocHistory.setSpeed(new BigDecimal(8));
        vocHistory.setPressure(new BigDecimal(900));
        vocHistory.setTemp(new BigDecimal(29));

        vocHistory.setVocFlag("N");
        vocHistory.setFlowFlag("N");
        vocHistory.setSpeedFlag("N");
        vocHistory.setPressureFlag("N");
        vocHistory.setTempFlag("N");

        stringRedisTemplate.boundHashOps(TableUtil.Voc_RealTime).put(vocHistory.getDeviceNo(), JSON.toJSONString(vocHistory));
    }

    @Test
    public void test02(){
        VocHistory vocHistory = new VocHistory();

        vocHistory.setDeviceNo("2019103000001");
        vocHistory.setDateTime(LocalDateTime.now());

        vocHistory.setVoc(new BigDecimal(5.6));
        vocHistory.setFlow(new BigDecimal(60));
        vocHistory.setSpeed(new BigDecimal(8));
        vocHistory.setPressure(new BigDecimal(900));
        vocHistory.setTemp(new BigDecimal(29));

        vocHistory.setVocFlag("N");
        vocHistory.setFlowFlag("T");
        vocHistory.setSpeedFlag("N");
        vocHistory.setPressureFlag("T");
        vocHistory.setTempFlag("N");

        stringRedisTemplate.boundHashOps(TableUtil.Voc_Alarm).put(vocHistory.getDeviceNo(), JSON.toJSONString(vocHistory));
    }
}
