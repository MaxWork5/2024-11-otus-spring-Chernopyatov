package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private static final String ERROR_MESSAGE = "The entered value does not match the suggested options.";

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        ioService.printFormattedLine("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);
        for (var question: questions) {
            var isAnswerValid = false;
            // Задать вопрос, получить ответ
            showFormatedTestQuestion(question);
            try {
                var userAnswer = ioService.readIntForRange(1, question.answers().size(), ERROR_MESSAGE);
                isAnswerValid = question.answers().get(userAnswer - 1).isCorrect();
            } catch (IllegalArgumentException e) {
                ioService.printFormattedLine("%s%n", e.getMessage());
            }
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private void showFormatedTestQuestion(Question question) {
        ioService.printFormattedLine("%s%n", question.text());
        var identificatorAnswer = 0;
        for (var answers : question.answers()) {
            identificatorAnswer++;
            ioService.printFormattedLine("%d. %s%n", identificatorAnswer, answers.text());
        }
        ioService.printFormattedLine("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~%n");
    }
}