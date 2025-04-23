package ru.otus.hw.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    @GetMapping("/greeting")
    public String helloWorld() {
        return "Hello ";
    }

    @GetMapping("/delay/greeting")
    public String delayGreeting() throws InterruptedException {
        Thread.sleep(50000);
        return "Hello ";
    }
}
