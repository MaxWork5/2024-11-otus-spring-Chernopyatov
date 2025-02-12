package ru.otus.hw.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;

@ExtendWith(MockitoExtension.class)
class StreamsIOServiceTest {

    @Mock
    private PrintStream printStream;

    private StreamsIOService streamsIOService;

    @BeforeEach
    void setUp() {
        streamsIOService = new StreamsIOService(printStream);
    }

    @Test
    void printLine() {
        Assertions.assertDoesNotThrow(() -> {streamsIOService.printLine("hello world");});
    }

    @Test
    void printFormattedLine() {
        Assertions.assertDoesNotThrow(() -> {streamsIOService.printFormattedLine("hello world");});
    }
}