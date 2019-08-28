package com.osen.cloud.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: PangYi
 * Date: 2019-08-28
 * Time: 17:56
 * Description:
 */
@RestController
public class HelloController {

    @GetMapping("/test")
    public String hello() {
        int i = 0;
        i = 10 / i;
        return "hehe";
    }
}
