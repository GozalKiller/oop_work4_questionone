package org.example;

import org.example.Event;
import org.example.EventSubscriber;

import java.util.*;

public class Partition {
    private final String name;
    private final Event[] events;
    private int writeIndex = 0;
    private final Map<EventSubscriber, Integer> readPointers = new HashMap<>();
    private final Map<EventSubscriber, Set<String>> subscriberEventTypes = new HashMap<>();

    public Partition(String name, int size) {
        this.name = name;
        this.events = new Event[size];
    }

    public void publish(Event event) {
        events[writeIndex] = event;
        writeIndex = (writeIndex + 1) % events.length;
    }

    public void subscribe(String eventType, EventSubscriber subscriber) {
        readPointers.putIfAbsent(subscriber, 0);
        subscriberEventTypes.putIfAbsent(subscriber, new HashSet<>());
        subscriberEventTypes.get(subscriber).add(eventType);
    }

    public void consume(EventSubscriber subscriber) {
        int index = readPointers.getOrDefault(subscriber, 0);
        for (int i = 0; i < events.length; i++) {
            Event event = events[index];
            if (event != null && subscriberEventTypes.get(subscriber).contains(event.eventType)) {
                subscriber.handleEvent(event.eventType, event.data);
                readPointers.put(subscriber, (index + 1) % events.length);
                return;
            }
            index = (index + 1) % events.length;
        }
    }

    public void rollback(EventSubscriber subscriber) {
        int index = readPointers.getOrDefault(subscriber, 0);
        int newIndex = (index - 1 + events.length) % events.length;
        readPointers.put(subscriber, newIndex);
    }
    public void printAllEvents() {
        System.out.println("Events in partition '" + name + "':");
        for (int i = 0; i < events.length; i++) {
            Event event = events[i];
            if (event != null) {
                System.out.println("- [" + i + "] " + event.eventType + ": " + event.data);
            }
        }
    }

}
