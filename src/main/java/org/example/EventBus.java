package org.example;

import org.example.EventSubscriber;


import java.util.*;

public class EventBus {
    private static final Map<String, Partition> partitions = new HashMap<>();

    public static void createPartition(String name, int size) {
        partitions.put(name, new Partition(name, size));
    }

    public static void publish(String eventType, String data, String partitionName) {
        Partition p = partitions.get(partitionName);
        if (p != null) {
            p.publish(new Event(eventType, data));
        } else {
            System.out.println("Partition not found: " + partitionName);
        }
    }

    public static void subscribe(String partitionName, String eventType, EventSubscriber subscriber) {
        Partition p = partitions.get(partitionName);
        if (p != null) {
            p.subscribe(eventType, subscriber);
        } else {
            System.out.println("Partition not found: " + partitionName);
        }
    }

    public static void consume(String partitionName, EventSubscriber subscriber) {
        Partition p = partitions.get(partitionName);
        if (p != null) {
            p.consume(subscriber);
        }
    }

    public static void rollback(String partitionName, EventSubscriber subscriber) {
        Partition p = partitions.get(partitionName);
        if (p != null) {
            p.rollback(subscriber);
        }
    }
    public static Event createEvent(String eventType, String data) {
        return new Event(eventType, data);
    }
    public static void publishEvent(Event event, String partitionName) {
        Partition p = partitions.get(partitionName);
        if (p != null) {
            p.publish(event);
        } else {
            System.out.println("Partition not found: " + partitionName);
        }
    }
    public static void printPartitionEvents(String partitionName) {
        Partition p = partitions.get(partitionName);
        if (p != null) {
            p.printAllEvents();
        } else {
            System.out.println("Partition not found: " + partitionName);
        }
    }
}
