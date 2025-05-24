package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.service.ApplicationService;

@RestController
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService service;

    @GetMapping(value = "/hello")
    public String hello(@RequestParam String name, @RequestParam Boolean delayed) {
        return service.doSomething(delayed) + name;
    }
}
