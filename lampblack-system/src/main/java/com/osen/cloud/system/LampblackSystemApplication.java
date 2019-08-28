package com.osen.cloud.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.osen.cloud")
// 扫描Mapper配置注解
@MapperScan(basePackages = {"com.osen.cloud.model"})
// 开启事务处理
@EnableTransactionManagement
public class LampblackSystemApplication {

    /**
     * 全局唯一入口文件
     *
     * @param args 系统参数
     */
    public static void main(String[] args) {
        SpringApplication.run(LampblackSystemApplication.class, args);
    }

}
