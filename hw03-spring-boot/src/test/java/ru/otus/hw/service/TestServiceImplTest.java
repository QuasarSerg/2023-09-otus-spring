package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestServiceImplTest {

    private TestService testService;

    @BeforeEach
    void setUp() {
        LocalizedIOService localizedIOServiceImpl = Mockito.mock(LocalizedIOServiceImpl.class);
        Mockito.when(localizedIOServiceImpl.readStringWithPrompt("Which plant knows everything? (Groot, Horseradish)"))
                .thenReturn("Horseradish");
        Mockito.when(localizedIOServiceImpl.readStringWithPrompt("Who walks sitting? (Chess player, Duck)"))
                .thenReturn("Chess player");
        List<Question> questionList = List.of(
                new Question("Which plant knows everything?", List.of(
                        new Answer("Groot", false),
                        new Answer("Horseradish", true))),
                new Question("Who walks sitting?", List.of(
                        new Answer("Chess player", true),
                        new Answer("Duck", false))));
        QuestionDao questionDao = Mockito.mock(QuestionDao.class);
        Mockito.when(questionDao.findAll()).thenReturn(questionList);

        testService = new TestServiceImpl(localizedIOServiceImpl, questionDao);
    }

    @Test
    void executeTestFor() {
        Student student = new Student("Мак", "Сим");
        TestResult testResult = testService.executeTestFor(student);
        assertEquals(2, testResult.getRightAnswersCount());
    }
}