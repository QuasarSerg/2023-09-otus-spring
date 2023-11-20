package ru.otus.hw.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TestRunnerEvent extends ApplicationEvent {

    private final String[] args;

    public TestRunnerEvent(Object source, String... args) {
        super(source);
        this.args = args;
    }
}
