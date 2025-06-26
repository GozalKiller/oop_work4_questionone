package org.example;

import org.example.Event;
import org.example.EventSubscriber;

import java.util.function.BiConsumer;

public class SpecificEventSubscriber implements EventSubscriber {
    private final String name;
    private final BiConsumer<String, String> callback;

    public SpecificEventSubscriber(String name, BiConsumer<String, String> callback) {
        this.name = name;
        this.callback = callback;
    }

    public String getName() {
        return name;
    }



    @Override
    public void handleEvent(String eventType, String data) {
        System.out.println("[" + name + "] Received event: " + eventType + ", data: " + data);
        callback.accept(eventType, data);
    }
}
