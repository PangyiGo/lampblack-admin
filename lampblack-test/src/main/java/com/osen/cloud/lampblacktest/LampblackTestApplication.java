package com.osen.cloud.lampblacktest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.osen.cloud")
// 扫描Mapper配置注解
@MapperScan(basePackages = {"com.osen.cloud.model"})
// 开启事务处理
@EnableTransactionManagement
public class LampblackTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(LampblackTestApplication.class, args);
    }

}
