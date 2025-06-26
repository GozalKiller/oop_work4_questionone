package org.example;

import org.example.EventBus;
import org.example.Publisher;
import org.example.SpecificEventSubscriber;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // חלק 1: הדגמה אוטומטית
        System.out.println("===== PART 1: example of 2 subs getting events from publisher, and making rollback =====");

        SpecificEventSubscriber sub1 = new SpecificEventSubscriber("Sub1", (eventType, data) ->
                System.out.println("[Sub1] Received: " + eventType + " | " + data));
        SpecificEventSubscriber sub2 = new SpecificEventSubscriber("Sub2", (eventType, data) ->
                System.out.println("[Sub2] Received: " + eventType + " | " + data));

        Publisher pub = new Publisher("Pub1");

        EventBus.createPartition("Orders", 5);
        EventBus.subscribe("Orders", "OrderPlaced", sub1);
        EventBus.subscribe("Orders", "OrderPlaced", sub2);

        pub.publish("OrderPlaced", "Order#100", "Orders");
        pub.publish("OrderPlaced", "Order#101", "Orders");
        pub.publish("OrderPlaced", "Order#102", "Orders");

        System.out.println("\n--- Sub1 Consumes 2 events ---");
        EventBus.consume("Orders", sub1);
        EventBus.consume("Orders", sub1);

        System.out.println("\n--- Sub1 does Rollback ---");
        EventBus.rollback("Orders", sub1);

        System.out.println("\n--- Sub1 Consumes again ---");
        EventBus.consume("Orders", sub1);

        System.out.println("\n--- Sub2 Consumes ---");
        EventBus.consume("Orders", sub2);

        System.out.println("\n--- All Events in 'Orders' Partition ---");
        EventBus.printPartitionEvents("Orders");

        // חלק 2: תפריט אינטראקטיבי
        System.out.println("\n===== PART 2: Interactive Menu =====");

        while (true) {
            System.out.println("\n====== Menu ======");
            System.out.println("1. Create Partition");
            System.out.println("2. Publish Event");
            System.out.println("3. Subscribe sub1/sub2 to Partition");
            System.out.println("4. Consume Event (sub1 or sub2)");
            System.out.println("5. Rollback (sub1 or sub2)");
            System.out.println("6. Print Partition Events");
            System.out.println("7. Exit");
            System.out.print("Your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Partition name: ");
                    String pname = scanner.nextLine();
                    System.out.print("Partition size: ");
                    int size = Integer.parseInt(scanner.nextLine());
                    EventBus.createPartition(pname, size);
                    break;

                case "2":
                    System.out.print("Partition to publish to: ");
                    String publishPart = scanner.nextLine();
                    System.out.print("Event type: ");
                    String etype = scanner.nextLine();
                    System.out.print("Event data: ");
                    String edata = scanner.nextLine();
                    pub.publish(etype, edata, publishPart);
                    break;

                case "3":
                    System.out.print("Subscriber (1 or 2): ");
                    String subChoice = scanner.nextLine();
                    EventSubscriber sub = subChoice.equals("1") ? sub1 : sub2;
                    System.out.print("Partition: ");
                    String part = scanner.nextLine();
                    System.out.print("Event type: ");
                    String evtype = scanner.nextLine();
                    EventBus.subscribe(part, evtype, sub);
                    break;

                case "4":
                    System.out.print("Subscriber (1 or 2): ");
                    String subC = scanner.nextLine();
                    EventSubscriber subConsume = subC.equals("1") ? sub1 : sub2;
                    System.out.print("Partition: ");
                    String cp = scanner.nextLine();
                    EventBus.consume(cp, subConsume);
                    break;

                case "5":
                    System.out.print("Subscriber (1 or 2): ");
                    String subR = scanner.nextLine();
                    EventSubscriber subRollback = subR.equals("1") ? sub1 : sub2;
                    System.out.print("Partition: ");
                    String rp = scanner.nextLine();
                    EventBus.rollback(rp, subRollback);
                    break;

                case "6":
                    System.out.print("Partition name: ");
                    String pp = scanner.nextLine();
                    EventBus.printPartitionEvents(pp);
                    break;

                case "7":
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}


