package com.zhangfeng.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@RestController
public class ZuulServerApplication {

    @Value("${spring.application.name:aa}")
    private String appname;

    @Value("${server.port:1}")
    private String port;

    public static void main(String[] args) {
        SpringApplication.run(ZuulServerApplication.class, args);
    }

    @RequestMapping("/hello")
    public String hello(){
        return String.format("from %s:%s", appname, port);
    }
}
