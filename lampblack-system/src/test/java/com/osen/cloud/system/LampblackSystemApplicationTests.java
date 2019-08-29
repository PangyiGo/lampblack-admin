package com.osen.cloud.system;

import com.osen.cloud.common.entity.User;
import com.osen.cloud.service.authorization.AuthorizationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LampblackSystemApplicationTests {

    @Autowired
    private AuthorizationService authorizationService;

    @Test
    public void contextLoads() {
        User test01 = authorizationService.findByUsername("test01");

        System.out.println(test01);
    }

}
