package com.osen.cloud.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 扫描Mapper配置注解
@MapperScan(basePackages = {"com.osen.cloud.model"})
public class LampblackSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LampblackSystemApplication.class, args);
    }

}
