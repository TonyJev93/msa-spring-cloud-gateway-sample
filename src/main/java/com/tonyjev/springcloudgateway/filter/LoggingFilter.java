package com.tonyjev.springcloudgateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

// application.yml 에 해당 필터 추가 필요.
@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
//        return ((exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//
//            log.info("Global filter baseMessage : {}", config.getBaseMessage());
//
//            if (config.isPreLogger()) {
//                log.info("Global filter start : request id  -> {}", request.getId());
//            }
//            // Custom Post Filter
//            return chain.filter(exchange).then(Mono.fromRunnable(() -> { // Mono(웹플럭스 스프링5 추가) : 비동기 방식 단일값 전달 시 사용
//                if (config.isPostLogger()) {
//                    log.info("Global filter end : response code -> {}", response.getStatusCode());
//                }
//            }));
//        });

        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Logging filter baseMessage : {}", config.getBaseMessage());

            if (config.isPreLogger()) {
                log.info("Logging PRE filter : request id  -> {}", request.getId());
            }
            // Custom Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> { // Mono(웹플럭스 스프링5 추가) : 비동기 방식 단일값 전달 시 사용
                        if (config.isPostLogger()) {
                            log.info("Logging POST filter : response code -> {}", response.getStatusCode());
                        }
                    }
            ));
//        }, Ordered.HIGHEST_PRECEDENCE); // Logging 이 Global, Custom 보다 우선순위 높음
        }, Ordered.LOWEST_PRECEDENCE); // Logging 이 Global, Custom 보다 우선순위 낮음

        return filter;
    }

    @Data
    public static class Config {
        // Put the configuration properties
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
