package com.inno.sc.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("statdata-service")
public interface StatdataServiceClient {

    @GetMapping(value="/api/hello")
    String hello();

}
