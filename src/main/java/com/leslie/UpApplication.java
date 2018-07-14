package com.leslie;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
@RestController
@EnableSwagger2
@Slf4j
@EnableFeignClients
@ImportResource("classpath:spring-config.xml")
public class UpApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(UpApplication.class).run(args);
    }

    @Bean
    public Docket swaggerDocket() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.basePackage(getClass().getPackage().getName())).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().contact(new Contact("杨海校", "http://184.170.216.35/", "yanghx_pro@163.com")).title("秀（Xiu）Restful Api文档").description("提供api接口的查询与调试")
                .termsOfServiceUrl("http://184.170.216.35/").version("0.1").build();
    }



    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }
}
