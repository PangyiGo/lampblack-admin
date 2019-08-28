package com.osen.cloud.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * User: PangYi
 * Date: 2019-08-28
 * Time: 15:44
 * Description: swagger 接口文档配置
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket controllerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(new ApiInfoBuilder()
                    .title("奥斯恩油烟浓度平台_接口文档")
                    .description("开发油烟浓度在线监控平台，模块管理")
                    .contact(new Contact("Pangyi", null, null))
                    .version("版本号:V1.0.0")
                    .build())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.osen.cloud.system"))
            .paths(PathSelectors.any())
            .build();
    }
}
