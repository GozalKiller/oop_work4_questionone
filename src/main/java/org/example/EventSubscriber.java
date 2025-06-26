package org.example;

public interface EventSubscriber {
    void handleEvent(String eventType, String data);
}

