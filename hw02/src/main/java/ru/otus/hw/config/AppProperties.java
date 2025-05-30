package ru.otus.hw.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AppProperties implements TestConfig, TestFileNameProvider {

    // внедрить свойство из application.properties
    private int rightAnswersCountToPass;

    // внедрить свойство из application.properties
    private String testFileName;

    public AppProperties(@Value("${test.rightAnswersCountToPass}")int rightAnswersCountToPass,
                         @Value("${test.fileName:questions.csv}") String testFileName) {
        this.rightAnswersCountToPass = rightAnswersCountToPass;
        this.testFileName = testFileName;
    }
}
