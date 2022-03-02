package com.inno.sc.statdata.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatdataServiceController {


    @Value("${server.port}")
    private String serverPort;

    @GetMapping("api/hello")
    public String hello() {

        return "hello Statdata Service " + serverPort;
    }

}
