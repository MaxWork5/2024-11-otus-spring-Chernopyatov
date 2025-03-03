package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "test-runner-service.enabled", havingValue = "true")
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    @Override
    public void run(ApplicationArguments args) {
        var student = studentService.determineCurrentStudent();
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
    }
}