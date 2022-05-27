package com.inno.sc.ecorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OrderServiceAppliation {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceAppliation.class, args);
    }
}
