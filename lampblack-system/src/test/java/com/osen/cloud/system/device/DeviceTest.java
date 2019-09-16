package com.osen.cloud.system.device;

import com.osen.cloud.service.device.DeviceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * User: PangYi
 * Date: 2019-09-16
 * Time: 15:50
 * Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceTest {

    @Autowired
    private DeviceService deviceService;

    @Test
    public void test01() {
        boolean status = deviceService.updateDeviceStatus(2, "2019051703100020");

        System.out.println(status);
    }
}