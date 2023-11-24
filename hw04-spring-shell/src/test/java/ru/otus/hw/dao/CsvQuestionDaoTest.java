package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CsvQuestionDao.class)
class CsvQuestionDaoTest {

    @Autowired
    private QuestionDao csvQuestionDao;

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