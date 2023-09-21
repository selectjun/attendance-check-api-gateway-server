package com.attendancecheck.gatewayserver.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

/**
 * 사전 필터 유틸
 */
@Component
public class FilterUtils {

    /**
     * tmx-correlation-id 헤더 키
     */
    public static final String CORRELATION_ID = "tmx-correlation-id";

    /**
     * Authorization 헤더 키
     */
    public static final String AUTH_TOKEN = "Authorization";

    /**
     * 사전 필터 키
     */
    public static final String PRE_FILTER_TYPE = "pre";

    /**
     * 사후 필터 키
     */
    public static final String POST_FILTER_TYPE = "post";

    /**
     * route 필터 키
     */
    public static final String ROUTE_FILTER_TYPE = "route";

    /**
     * tmx-correlation-id 추출 하기
     * @param requestHeaders 요청 헤더
     * @return tmx-correlation-id
     */
    public String getCorrelationId(HttpHeaders requestHeaders){
        if (requestHeaders.get(CORRELATION_ID) != null) {
            List<String> header = requestHeaders.get(CORRELATION_ID);
            return header.stream().findFirst().get();
        } else {
            return null;
        }
    }

    /**
     * Authorization 추출 하기
     * @param requestHeaders 요청 헤더
     * @return Authorization
     */
    public String getAuthToken(HttpHeaders requestHeaders){
        if (requestHeaders.get(AUTH_TOKEN) !=null) {
            List<String> header = requestHeaders.get(AUTH_TOKEN);
            return header.stream().findFirst().get();
        } else {
            return null;
        }
    }

    /**
     * 헤더 설정 하기
     * @param exchange ServerWebExchange
     * @param name 이름
     * @param value 값
     * @return ServerWebExchange
     */
    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
        return exchange.mutate().request(
                exchange.getRequest().mutate()
                        .header(name, value)
                        .build())
                .build();
    }

    /**
     * tmx-correlation-id 설정 하기
     * @param exchange ServerWebExchange
     * @param correlationId tmx-correlation-id
     * @return ServerWebExchange
     */
    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
        return this.setRequestHeader(exchange, CORRELATION_ID, correlationId);
    }

}
