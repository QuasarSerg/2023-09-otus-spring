package ru.otus.hw.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class TestServiceImplTest {

    private final PrintStream standardOut = System.out;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();


    private TestService  testServiceImpl;


    @BeforeEach
    public void setUp() {
        PrintStream printStream = new PrintStream(outputStreamCaptor);
        System.setOut(printStream);

        List<Question> questionList = List.of(
                new Question("Question1", List.of(
                        new Answer("Answer1", false),
                        new Answer("Answer2", true))));

        QuestionDao csvQuestionDao = mock(QuestionDao.class);
        when(csvQuestionDao.findAll()).thenReturn(questionList);
        testServiceImpl = new TestServiceImpl(new StreamsIOService(printStream), csvQuestionDao);
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void executeTest() {
        String expectedMessage = "Please answer the questions below\r\n\r\nQuestion1 (Answer1, Answer2)";

        testServiceImpl.executeTest();
        Assertions.assertEquals(expectedMessage, outputStreamCaptor.toString().trim());
    }
}