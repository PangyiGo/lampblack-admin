package com.osen.cloud.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * User: PangYi
 * Date: 2019-08-28
 * Time: 8:49
 * Description: Swagger UI接口文档配置
 */
@Configuration
@EnableSwagger2
public class SwaggerUIConfig {

    /**
     * 添加摘要信息(Docket)
     */
    @Bean
    public Docket controllerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(new ApiInfoBuilder()
                    .title("深圳奥斯恩净化技术有限公司-接口文档")
                    .description("油烟浓度在线监控平台")
                    .contact(new Contact("PangYi", null, null))
                    .version("版本号:V1.0")
                    .build())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.osen.cloud"))
            .paths(PathSelectors.any())
            .build();
    }
}
