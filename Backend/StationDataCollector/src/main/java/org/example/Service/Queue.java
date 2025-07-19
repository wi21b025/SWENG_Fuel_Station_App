package org.example.Service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.concurrent.TimeoutException;


public class Queue {

    private final static String CONSUME = "DCD_SDC";
    private final static String PRODUCE = "SDC_DCR";

    private final static String HOST = "localhost";

    private final static int PORT = 30003;

    private int id;

    private static ConnectionFactory factory;

    public Queue() {
        factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(PORT);
    }

    public void receive() throws IOException, TimeoutException {
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(CONSUME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "' " + LocalTime.now());

            float kwh = getKWH(message);
            try {
                send(kwh);
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }
        };

        channel.basicConsume(CONSUME, true, deliverCallback, consumerTag -> {});
    }
    private void send(float kwh) throws IOException, TimeoutException {
        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()
        ) {
            channel.queueDeclare(PRODUCE, false, false, false, null);
            String message = String.format("id=%s&kwh=%.1f", id, kwh);
            channel.basicPublish("", PRODUCE, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(message);
        }
    }

    public float getKWH(String message) {
        String[] parts = message.split("db_url=localhost:|&id=");
        int port = Integer.parseInt(parts[1]);
        this.id = Integer.parseInt(parts[2]);
        Database db = new Database(port);
        return db.select(id);
    }
}
