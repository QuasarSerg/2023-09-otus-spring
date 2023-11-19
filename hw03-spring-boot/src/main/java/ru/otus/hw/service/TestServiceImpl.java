package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            var answerToQuestion = ioService.readStringWithPrompt(getQuestionText(question));
            testResult.applyAnswer(question, isAnswerValid(question, answerToQuestion));
        }
        return testResult;
    }

    private boolean isAnswerValid(Question question, String answerToQuestion) {
        Answer answer = question.answers().stream()
                .filter(a -> a.text().equalsIgnoreCase(answerToQuestion))
                .findFirst().orElse(null);
        return answer != null && Boolean.TRUE.equals(answer.isCorrect());
    }

    private String getQuestionText(Question question) {
        String curAnswers = question.answers().stream()
                .map(Answer::text)
                .collect(Collectors.joining(", "));
        return String.format("%s (%s)", question.text(), curAnswers);
    }
}
