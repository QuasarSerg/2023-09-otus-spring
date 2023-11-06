package ru.otus.hw.dao.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.domain.Answer;

import static org.junit.jupiter.api.Assertions.*;

class AnswerCsvConverterTest {

    private AnswerCsvConverter answerCsvConverter;

    @BeforeEach
    void setUp() {
        answerCsvConverter = new AnswerCsvConverter();
    }

    @DisplayName("Конвертация строки в объект Answer")
    @Test
    void convertToRead() {
        Answer answer = answerCsvConverter.convertToRead("This is Sparta!%true");
        assertEquals("This is Sparta!", answer.text());
        assertTrue(answer.isCorrect());
    }
}