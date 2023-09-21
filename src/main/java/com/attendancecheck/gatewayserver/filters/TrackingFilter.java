package com.attendancecheck.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * 사전 필터
 */
@Order(1)
@Component
public class TrackingFilter implements GlobalFilter {

    /**
     * 로거
     */
    private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);

    /**
     * 필터 유틸
     */
    @Autowired
    FilterUtils filterUtils;

    /**
     * 필터
     * @param exchange the current server exchange
     * @param chain provides a way to delegate to the next filter
     * @return Mono
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders =
                exchange.getRequest().getHeaders();
        if (isCorrelationIdPresent(requestHeaders)) {
            logger.debug("tmx-correlation-id found in tracking filter: {}. ",
                    filterUtils.getCorrelationId(requestHeaders));
        } else {
            String correlationID = generateCorrelationId();
            exchange = filterUtils.setCorrelationId(exchange, correlationID);
            logger.debug(
                    "tmx-correlation-id generated in tracking filter: {}.",
                    correlationID);
        }
        return chain.filter(exchange);
    }

    /**
     * tmx-correlation-id 존재 여부 확인
     * @param requestHeaders 요청 헤더
     * @return tmx-correlation-id 존재 여부
     */
    private boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
        if (filterUtils.getCorrelationId(requestHeaders) != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * tmx-correlation-id 생성
     * @return tmx-correlation-id
     */
    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }

}
