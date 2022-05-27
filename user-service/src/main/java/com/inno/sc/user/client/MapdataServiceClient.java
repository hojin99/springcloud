package com.inno.sc.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("mapdata-service")
public interface MapdataServiceClient {

    @GetMapping(value="/api/hello")
    String hello();

}
