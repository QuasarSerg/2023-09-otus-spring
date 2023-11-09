package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.config.AppConfig;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;


import static org.junit.jupiter.api.Assertions.*;

class TestServiceImplTest {

    TestService testService;

    @BeforeEach
    void setUp() {
        IOService ioService = Mockito.mock(StreamsIOService.class);
        Mockito.when(ioService.readStringWithPrompt("Which plant knows everything? (Groot, Horseradish)"))
                .thenReturn("Horseradish");
        Mockito.when(ioService.readStringWithPrompt("Who walks sitting? (Chess player, Duck)"))
                .thenReturn("Chess player");
        TestFileNameProvider testFileNameProvider = Mockito.mock(AppConfig.class);
        Mockito.when(testFileNameProvider.getTestFileName()).thenReturn("questions.csv");
        QuestionDao questionDao = new CsvQuestionDao(testFileNameProvider);

        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    void executeTestFor() {
        Student student = new Student("Мак", "Сим");
        TestResult testResult = testService.executeTestFor(student);
        assertEquals(2, testResult.getRightAnswersCount());
    }
}