package com.inno.sc.mapdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MapdataServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapdataServiceApplication.class, args);
    }

}
