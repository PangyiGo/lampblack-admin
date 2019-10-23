package com.osen.cloud.system.user;

import com.osen.cloud.common.entity.system_user.Role;
import com.osen.cloud.common.entity.system_user.User;
import com.osen.cloud.service.role.RoleService;
import com.osen.cloud.service.user.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;

/**
 * User: PangYi
 * Date: 2019-09-03
 * Time: 15:13
 * Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserSimpleTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Test
    public void test01() {
        User user = userService.findByUsername("test01");
        Assert.assertNotNull(user);
    }

    @Test
    public void test02() {
    }

    @Test
    public void test03() {
        Integer[] integers = {1, 2, 5};
        Collection<Role> byIds = roleService.listByIds(Arrays.asList(integers));
        System.out.println(byIds);
    }

    @Test
    public void test04() {
        User user = new User();
        user.setAccount("奥斯恩环保");
        user.setPassword("0000");
        user.setPhone("15920362472");
        user.setEmail("1306359812@qq.com");
        user.setAddress("广东省深圳市宝安区福永街道凤凰工业园");

        boolean b = userService.updateUserToAccount(user);
        System.out.println(b);
    }
}
