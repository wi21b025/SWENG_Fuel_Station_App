package org.example.Service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Queue {

    private final static String CONSUME1 = "DCD_DCR";
    private final static String CONSUME2 = "SDC_DCR";
    private final static String PRODUCE = "DCR_PG";
    private final static String HOST = "localhost";
    private final static int PORT = 30003;
    private int expectedCount;
    private int receivedCount = 0;

    private static ConnectionFactory factory;

    public Queue() {
        factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(PORT);
    }

    public void consumeExpectedMessages() throws IOException, TimeoutException {
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(CONSUME1, false, false, false, null);

        System.out.println(" [*] Waiting for messages from DataCollectionDispatcher. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received from DataCollectionDispatcher: '" + message + "' " + LocalTime.now());


            String[] keyValuePairs = message.split("&");
            for (String keyValuePair : keyValuePairs) {
                String[] parts = keyValuePair.split("=");
                if (parts.length == 2) {
                    String key = parts[0];
                    String value = parts[1];
                    if (key.equals("count")) {
                        expectedCount = Integer.parseInt(value);
                    }
                }
            }
            System.out.println("expCount " + expectedCount);
        };

        channel.basicConsume(CONSUME1, true, deliverCallback, consumerTag -> {
        });
    }

    public void consumeActualMessages() throws IOException, TimeoutException {
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(CONSUME2, false, false, false, null);

        System.out.println(" [*] Waiting for messages from StationDataCollector. To exit press CTRL+C");

        List<String> receivedMessages = new ArrayList<>();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received from StationDataCollector: '" + message + "' " + LocalTime.now());
            receivedMessages.add(message);
            receivedCount++;
            // Check if the number of received messages from CONSUME2 matches the expected number
            if (expectedCount == receivedCount) {
                System.out.println("got this " + receivedMessages);
                try {
                    System.out.println("calculating " + receivedMessages);
                    String customerTotal = calculateTotal(receivedMessages);
                    send(customerTotal);
                    receivedCount = 0;
                    receivedMessages.clear();
                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        channel.basicConsume(CONSUME2, true, deliverCallback, consumerTag -> {
        });
    }

    private void send(String customerData) throws IOException, TimeoutException {
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            System.out.println("Publishing " + customerData);
            channel.queueDeclare(PRODUCE, false, false, false, null);
            channel.basicPublish("", PRODUCE, null, customerData.getBytes(StandardCharsets.UTF_8));
            System.out.println(" Sent: '" + customerData + "' to PDFGenerator");
        }
    }

    public String calculateTotal(List<String> receivedMessages) {
        float totalKWH = 0;
        String id = null;

        for (String message : receivedMessages) {
            String[] keyValuePairs = message.split("&");
            for (String keyValuePair : keyValuePairs) {
                String[] parts = keyValuePair.split("=");
                if (parts.length == 2) {
                    String key = parts[0];
                    String value = parts[1];
                    if (key.equals("id")) {
                        id = value;
                    } else if (key.equals("kwh")) {
                        String cleanedValue = value.replaceAll(",", "."); // Remove commas
                        totalKWH += Float.valueOf(cleanedValue);
                    }
                }
            }
        }
        if (id != null) {
            System.out.println(totalKWH);
            return "id=" + id + "&totalKWH=" + totalKWH;
        }
        return null;
    }

}