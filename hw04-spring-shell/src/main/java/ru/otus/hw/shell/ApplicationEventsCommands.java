package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.service.TestRunnerService;


@ShellComponent
@RequiredArgsConstructor
public class ApplicationEventsCommands {

    private final TestRunnerService testRunnerService;

    private String userName;

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public String login(@ShellOption(defaultValue = "AnyUser") String userName) {
        this.userName = userName;
        return String.format("Добро пожаловать: %s", userName);
    }

    @ShellMethod(value = "Run command", key = {"r", "run"})
    @ShellMethodAvailability(value = "isRunCommandAvailable")
    public void run() {
        testRunnerService.run();
    }

    private Availability isRunCommandAvailable() {
        return userName == null ? Availability.unavailable("Сначала залогиньтесь") : Availability.available();
    }
}
