package com.inno.sc.gateway.filter;

import com.inno.sc.gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class CustomAuthFilter extends AbstractGatewayFilterFactory<CustomAuthFilter.Config> {


    public CustomAuthFilter() {
        super(Config.class);
    }

    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if(!request.getHeaders().containsKey("Authorization")) {
                return handleUnAuthorized(exchange);
            }

            List<String> token = request.getHeaders().get("Authorization");
            String tokenString = Objects.requireNonNull(token).get(0);

            if(!tokenString.startsWith("Bearer")) {
                return handleUnAuthorized(exchange);
            } else {
                String jws = tokenString.substring(7);
                log.info(jws);

                Claims claims = JwtUtil.validateJwt(jws);
                log.info(claims.getSubject());
            }

            return chain.filter(exchange);
        });

    }

    private Mono<Void> handleUnAuthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    public static class Config {

    }

}
