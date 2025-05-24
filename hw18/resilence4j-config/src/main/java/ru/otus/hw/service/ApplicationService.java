package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.rest.HelloWorldClient;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final HelloWorldClient helloWorldClient;

    public String doSomething(Boolean delayed) {
        var result = "";
        if (delayed) {
            result = helloWorldClient.delayGreeting();
        } else {
            result = helloWorldClient.greeting();
        }
        return result;
    }
}
