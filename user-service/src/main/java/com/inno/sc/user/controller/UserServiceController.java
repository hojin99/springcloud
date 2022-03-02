package com.inno.sc.user.controller;


import com.inno.sc.user.client.MapdataServiceClient;
import com.inno.sc.user.client.StatdataServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserServiceController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private MapdataServiceClient mapClient;

    @Autowired
    private StatdataServiceClient statClient;

    /*
        Eureka Server에 applicationName에 대한 서버정보 목록을 조회
     */
    @GetMapping("/api/discover/{applicationName}")
    public List<ServiceInstance> serviceInstanceByApplicationName(@PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }

    @GetMapping("/api/hello-map")
    public String helloMapdata() {
        return mapClient.hello();
    }

    @GetMapping("/api/hello-stat")
    public String helloStatdata() {
        return statClient.hello();
    }






}
