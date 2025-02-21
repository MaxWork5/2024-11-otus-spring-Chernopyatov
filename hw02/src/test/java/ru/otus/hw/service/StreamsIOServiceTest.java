package ru.otus.hw.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class StreamsIOServiceTest {

    @Mock
    private PrintStream printStream;
    private StreamsIOService streamsIOService;

    @BeforeEach
    void setUp() {
        streamsIOService = new StreamsIOService(printStream, new ByteArrayInputStream("0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11".getBytes(StandardCharsets.UTF_8)));
    }

    @DisplayName("должен выводить запись на экран")
    @Test
    void printLine() {
        String expectedArgument = "hello world";

        Assertions.assertDoesNotThrow(() -> streamsIOService.printLine(expectedArgument));

        Mockito.verify(printStream).println(expectedArgument);
    }

    @DisplayName("должен выводить отформатированную запись на экран")
    @Test
    void printFormattedLine() {
        String expectedArgument = "hello world";
        Assertions.assertDoesNotThrow(() -> streamsIOService.printFormattedLine(expectedArgument));

        Mockito.verify(printStream).printf(expectedArgument + "%n");
    }

    @DisplayName("должен считать введённый массив байт")
    @Test
    void readString() {
        assertEquals("0", streamsIOService.readString());
    }

    @DisplayName("должен вывести строку и считать массив байт")
    @Test
    void readStringWithPrompt() {
        assertEquals("0", streamsIOService.readStringWithPrompt("testString"));

        Mockito.verify(printStream).println("testString");
    }

    @DisplayName("должен вернуть введённое значение соответствующее диапазону")
    @Test
    void readIntForRange() {
        assertEquals(1, streamsIOService.readIntForRange(1,20,"testError"));
    }

    @DisplayName("должен отбросить ошибку при условии что все введённые значения не соответствуют диапазону")
    @Test
    void readIntForRangeNotInRangeThrowException() {
        assertThrows(IllegalArgumentException.class, () -> streamsIOService.readIntForRange(20,21,"testError"));
    }

    @DisplayName("должен вывести строку и вернуть введённое значение соответствующее диапазону")
    @Test
    void readIntForRangeWithPrompt() {
        assertEquals(0, streamsIOService.readIntForRangeWithPrompt(0,20, "testString","testError"));

        Mockito.verify(printStream).println("testString");
    }

    @DisplayName("должен вывести строку и отбросить ошибку при условии что все введённые значения не соответствуют диапазону")
    @Test
    void readIntForRangeWithPromptNotInRangeThrowException() {
        assertThrows(IllegalArgumentException.class, ()-> streamsIOService.readIntForRangeWithPrompt(20,21, "testString","testError"));

        Mockito.verify(printStream).println("testString");
    }
}