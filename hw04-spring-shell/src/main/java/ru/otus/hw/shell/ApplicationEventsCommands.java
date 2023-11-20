package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.events.EventsPublisher;


@ShellComponent
@RequiredArgsConstructor
public class ApplicationEventsCommands {

    private final EventsPublisher eventsPublisher;

    private String userName;

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public String login(@ShellOption(defaultValue = "AnyUser") String userName) {
        this.userName = userName;
        return String.format("Добро пожаловать: %s", userName);
    }

    @ShellMethod(value = "Publish event command", key = {"p", "pub", "publish"})
    @ShellMethodAvailability(value = "isPublishEventCommandAvailable")
    public void publishEvent(@ShellOption(defaultValue = "") String... args) {
        eventsPublisher.publish(args);
    }

    private Availability isPublishEventCommandAvailable() {
        return userName == null ? Availability.unavailable("Сначала залогиньтесь") : Availability.available();
    }
}
