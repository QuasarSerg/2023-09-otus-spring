package ru.otus.hw.events;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestRunnerEventPublisher implements EventsPublisher {

    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(String... args) {
        publisher.publishEvent(new TestRunnerEvent(this, args));
    }
}
