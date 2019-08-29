package com.osen.cloud.system;

import com.osen.cloud.common.entity.Role;
import com.osen.cloud.common.entity.User;
import com.osen.cloud.service.authorization.AuthorizationService;
import com.osen.cloud.service.role.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LampblackSystemApplicationTests {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private RoleService roleService;

    @Test
    public void contextLoads() {
        User test01 = authorizationService.findByUsername("test01");

        List<Role> roleByUserId = roleService.findRoleByUserId(test01.getId());

        System.out.println(roleByUserId);

        System.out.println(test01);
    }

}
