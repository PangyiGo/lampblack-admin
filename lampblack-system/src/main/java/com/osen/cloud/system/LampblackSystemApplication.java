package com.osen.cloud.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "com.osen.cloud")
// 扫描Mapper配置注解
@MapperScan(basePackages = {"com.osen.cloud.model"})
// 开启事务处理
@EnableTransactionManagement
// 开启接口文档配置
@EnableSwagger2
// 开启异步处理
@EnableAsync
// 开启定时任务
@EnableScheduling
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
