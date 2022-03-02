package com.inno.sc.statdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class StatdataServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatdataServiceApplication.class, args);
    }
}
