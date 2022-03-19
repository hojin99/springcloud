package com.inno.sc.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// routing 방법을 application.yml에 정의함 (아래와 같이 코드로도 정의 가능함)
//@Configuration
public class FilterConfig {

//    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/statdata/**")
                    .filters(f -> f.addRequestHeader("sc_req","statdata")
                                .addResponseHeader("sc_res","statdata"))
                    .uri("lb://STATDATA-SERVICE")
                )
                .route(r -> r.path("/mapdata/**")
                        .filters(f -> f.addRequestHeader("sc_req","mapdata")
                                .addResponseHeader("sc_res","mapdata"))
                        .uri("lb://MAPDATA-SERVICE")
                )
                .build();
    }
}
