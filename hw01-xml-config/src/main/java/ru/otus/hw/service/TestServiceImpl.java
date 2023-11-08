package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao csvQuestionDao;


    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> all = csvQuestionDao.findAll();
        for (Question question: all) {
            String answers = question.answers().stream()
                    .map(Answer::text)
                    .collect(Collectors.joining(", "));
            ioService.printFormattedLine(question.text() + " (%s)", answers);
        }
    }
}
