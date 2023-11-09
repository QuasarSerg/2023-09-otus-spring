package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            var isAnswerValid = false;

            String curAnswers = question.answers().stream()
                    .map(Answer::text)
                    .collect(Collectors.joining(", "));
            String questionText = String.format("%s (%s)", question.text(), curAnswers);
            var firstName = ioService.readStringWithPrompt(questionText);

            Answer answer = question.answers().stream()
                    .filter(a -> a.text().equals(firstName))
                    .findFirst().orElse(null);
            isAnswerValid = answer != null && Boolean.TRUE.equals(answer.isCorrect());

            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
