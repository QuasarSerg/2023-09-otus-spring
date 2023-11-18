package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvQuestionDaoTest {

    private QuestionDao csvQuestionDao;

    @BeforeEach
    void setUp() {
        TestFileNameProvider appProperties = Mockito.mock(AppProperties.class);
        Mockito.when(appProperties.getTestFileName()).thenReturn("questions.csv");
        csvQuestionDao = new CsvQuestionDao(appProperties);
    }

    @Test
    void findAll() {
        List<Question> all = csvQuestionDao.findAll();
        assertEquals(2, all.size());
        Question question = all.get(1);
        assertEquals("Who walks sitting?", question.text());
        List<Answer> answers = question.answers();
        assertEquals(2, answers.size());
        assertEquals("Duck", answers.get(1).text());
        assertFalse(answers.get(1).isCorrect());
    }
}