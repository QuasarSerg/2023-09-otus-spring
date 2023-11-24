package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.TestRunnerService;

import static java.util.Objects.isNull;


@ShellComponent
@RequiredArgsConstructor
public class ApplicationCommands {

    private final TestRunnerService testRunnerService;

    private final LocalizedIOService ioService;

    private String userName;

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public String login(@ShellOption(defaultValue = "AnyUser") String userName) {
        this.userName = userName;
        return ioService.getMessage("ApplicationCommands.welcome", userName);
    }

    @ShellMethod(value = "Run command", key = {"r", "run"})
    @ShellMethodAvailability(value = "isRunCommandAvailable")
    public void run() {
        testRunnerService.run();
    }

    private Availability isRunCommandAvailable() {
        if (isNull(userName)) {
            return Availability.unavailable(ioService.getMessage("ApplicationCommands.login.first"));
        } else {
            return Availability.available();
        }
    }
}
