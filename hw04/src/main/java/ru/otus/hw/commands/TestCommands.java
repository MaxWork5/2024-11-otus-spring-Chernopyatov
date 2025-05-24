package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestService;

@RequiredArgsConstructor
@ShellComponent
@SuppressWarnings("unused")
public class TestCommands {
    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    @ShellMethod(value = "Begin test", key = {"start", "st", "sr"})
    public void start() {
        var student = studentService.determineCurrentStudent();
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
    }
}
