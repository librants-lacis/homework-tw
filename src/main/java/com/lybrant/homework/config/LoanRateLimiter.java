package com.lybrant.homework.config;

import es.moki.ratelimitj.core.limiter.request.RequestLimitRule;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import es.moki.ratelimitj.inmemory.request.InMemorySlidingWindowRequestRateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

@Configuration
@ConfigurationProperties(prefix = "loan-rate-limiter")
public class LoanRateLimiter {

    @Value("${duration}")
    private int duration;

    @Value("${timeUnit}")
    private TimeUnit timeUnit;

    @Value("${limit}")
    private long limit;

    @Bean
    public RequestRateLimiter rateLimiter() {
        return new InMemorySlidingWindowRequestRateLimiter(
                new HashSet<>(
                        Collections.singletonList(
                                RequestLimitRule.of(1, TimeUnit.MINUTES, limit)
                        )
                )
        );
    }
}
