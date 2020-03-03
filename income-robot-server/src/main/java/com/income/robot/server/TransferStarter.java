package com.income.robot.server;


import com.bbin.common.feignInterceptor.FeignClientInterceptor;
import com.bbin.common.feignInterceptor.FeignTenantInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@ComponentScan(excludeFilters = {@Filter(type = FilterType.REGEX, pattern = {
        "com.bbin.common.rabbitmq.sms.*", "com.bbin.common.redis.*"})},
        basePackages = {
            "com.bbin.common",
                "com.income.robot",
        })
@MapperScan({"com.income.robot.code.mapper"})
@EnableEurekaClient
@EnableFeignClients(basePackages={"com.bbin.common.feign","com.income.robot.server.client"})
@SpringBootApplication
public class TransferStarter {
    public static void main(String[] args) {
      SpringApplication.run(TransferStarter.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate((ClientHttpRequestFactory)new OkHttp3ClientHttpRequestFactory());
    }

    @Bean
    public FeignClientInterceptor getFeignClientInterceptor() {
        return new FeignClientInterceptor();
    }

    @Bean// tenantId ,channelId
    public FeignTenantInterceptor getFeignTenantInterceptor(){
        return new FeignTenantInterceptor();
    }
}
