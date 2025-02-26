package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CsvQuestionDaoTest {
    private static CsvQuestionDao dao;

    @BeforeAll
    public static void setUp() {
        var fileNameByLocaleTag = Map.of("en-US", "questions.csv");
        var properties = new AppProperties();
        properties.setRightAnswersCountToPass(3);
        properties.setLocale("en-US");
        properties.setFileNameByLocaleTag(fileNameByLocaleTag);
        dao = new CsvQuestionDao(properties);
    }

    @DisplayName("должен преобразовать все вопросы из файла questions.csv в List<Questions>")
    @Test
    void findAll() {
        var expectedQuestions = List.of(new Question("Does the test work?",
                List.of(new Answer("Yes", true), new Answer("No",false))));
        List<Question> questions = dao.findAll();
        assertNotNull(questions);
        assertFalse(questions.isEmpty());
        assertEquals(expectedQuestions, questions);
    }
}