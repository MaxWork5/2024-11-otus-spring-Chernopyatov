package ru.otus.hw.rest;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "greeting", url = "http://localhost:8088")
public interface HelloWorldClient {
    @GetMapping(value = "/greeting")
    @RateLimiter(name = "defaultRateLimiter")
    String greeting();

    @GetMapping(value = "/delay/greeting")
    @RateLimiter(name = "defaultRateLimiter")
    String delayGreeting();
}
