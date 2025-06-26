package org.example;

public class Publisher {
    private final String name;

    public Publisher(String name) {
        this.name = name;
    }

    public void publish(String eventType, String data, String partitionName) {
        System.out.println("[" + name + "] Publishing event '" + eventType + "' with data '" + data + "' to partition '" + partitionName + "'");
        EventBus.publish(eventType, data, partitionName);
    }
}
