package com.inno.sc.gateway.filter;

import com.inno.sc.gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
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

    private final Environment env;

    public CustomAuthFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }

    public GatewayFilter apply(Config config) {
        // Pre Filter
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Custom Pre Filter: jwt auth check!");
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

                try {
                    Claims claims = JwtUtil.validateJwt(jws, env.getProperty("token.secret"));
                    log.info(claims.getSubject());
                } catch(Exception ex) {
                    return handleUnAuthorized(exchange);
                }
            }

            // Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {

                log.info("Custom Post Filter: response code -> {}", response.getStatusCode());
            }));
        });

    }

    // Mono, Flux 중 하나를 반환해야 함 - Spring Webflux
    private Mono<Void> handleUnAuthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    public static class Config {

    }

}
