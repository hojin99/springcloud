package com.inno.sc.gateway.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config>  {

    public LoggingFilter() {
        super(LoggingFilter.Config.class);
    }

    public GatewayFilter apply(LoggingFilter.Config config) {
//        // Pre Filter
//        return ((exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//
//            log.info("Logging Filter baseMessage {}", config.getBaseMessage());
//            if(config.isPreLogger()) {
//                log.info("Logging Filter Start : request Id : {}", request.getId());
//            }
//
//            // Post Filter
//            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//
//                if(config.isPostLogger()) {
//                    log.info("Logging Filter End: response code -> {}", response.getStatusCode());
//                }
//            }));
//        });

        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Logging Filter baseMessage {}", config.getBaseMessage());
            if(config.isPreLogger()) {
                log.info("Logging Filter Start : request Id : {}", request.getId());
            }

            // Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {

                if(config.isPostLogger()) {
                    log.info("Logging Filter End: response code -> {}", response.getStatusCode());
                }
            }));

        }, Ordered.LOWEST_PRECEDENCE);

        return filter;
    }

    private Mono<Void> handleUnAuthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }




}
