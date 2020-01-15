package com.osen.cloud.system.system_device;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.osen.cloud.common.entity.system_device.Device;
import com.osen.cloud.common.enums.MonthCode;
import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.service.device.DeviceService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Test
    public void test02() {
        String tableName = "data_history_201908";
        boolean time = ConstUtil.compareToTime(MonthCode.Lampblack.getMonth());
        System.out.println(time);
    }

    @Test
    public void test03() {
        LambdaQueryWrapper<Device> wrapper = Wrappers.<Device>lambdaQuery().select(Device::getProvince, Device::getCity, Device::getArea).groupBy(Device::getProvince, Device::getCity, Device::getArea);
        List<Device> address = deviceService.list(wrapper);

        List<Device> deviceList = deviceService.list();

        Map<String, Map<String, Map<String, List<Device>>>> collect = deviceList.stream().collect(Collectors.groupingBy(Device::getProvince, Collectors.groupingBy(Device::getCity, Collectors.groupingBy(Device::getArea))));

        System.out.println(collect.keySet());

        Map<String, Map<String, List<Device>>> mapMap = collect.get("");
        System.out.println(mapMap);

        System.out.println(mapMap.keySet());

        Map<String, List<Device>> listMap = mapMap.get("重庆市");
        System.out.println(listMap);

        System.out.println(listMap.keySet());

        List<Device> list = listMap.get("静安区");
        System.out.println(list);
    }

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Node {

    private String label;

    private List<Node> children;
}
