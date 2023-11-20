package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ComponentScan(value = "ru.otus.hw.service")
class TestServiceImplTest {

    @MockBean
    private LocalizedIOService localizedIOService;

    @MockBean
    private TestRunnerService testRunnerService;

    private final TestService testService;

    @Autowired
    TestServiceImplTest(TestService testService) {
        this.testService = testService;
    }


    @BeforeEach
    void setUp() {
        Mockito.when(localizedIOService.readStringWithPrompt("Which plant knows everything? (Groot, Horseradish)"))
                .thenReturn("Horseradish");
        Mockito.when(localizedIOService.readStringWithPrompt("Who walks sitting? (Chess player, Duck)"))
                .thenReturn("Chess player");
    }

    @Test
    void executeTestFor() {
        Student student = new Student("Мак", "Сим");
        TestResult testResult = testService.executeTestFor(student);
        assertEquals(2, testResult.getRightAnswersCount());
    }
}