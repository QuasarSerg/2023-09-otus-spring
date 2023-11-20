package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import ru.otus.hw.events.TestRunnerEvent;
import ru.otus.hw.exceptions.QuestionReadException;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements CommandLineRunner, ApplicationListener<TestRunnerEvent> {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    private final LocalizedIOService localizedIOService;


    @Override
    public void run(String... args) throws Exception {
        try {
            var student = studentService.determineCurrentStudent();
            var testResult = testService.executeTestFor(student);
            resultService.showResult(testResult);
        } catch (QuestionReadException e) {
            localizedIOService.printLine("Error reading questions");
        } catch (Throwable e) {
            localizedIOService.printLine("Unknown error");
        }
    }


    @Override
    public void onApplicationEvent(TestRunnerEvent testRunnerEvent) {
        String[] args = testRunnerEvent.getArgs();
        try {
            run(args);
        } catch (Exception e) {
            localizedIOService.printLine("Unknown error");
        }
    }
}
