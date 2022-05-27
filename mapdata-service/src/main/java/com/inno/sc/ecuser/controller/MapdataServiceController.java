package com.inno.sc.ecuser.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class MapdataServiceController {

    Environment env;

    public MapdataServiceController(Environment env) {
        this.env = env;
    }

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/api/hello")
    public String hello(@RequestHeader Map<String, Object> requestHeader) {

        if(requestHeader.get("sc_req") != null)
            log.info((String)requestHeader.get("sc_req"));

        return String.format("hello Mapdata Service %s", env.getProperty("local.server.port"));
    }

}
