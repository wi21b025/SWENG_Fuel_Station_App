package org.example.Service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.example.PdfGenerator.PDFGenerator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Queue {

    private final static String CONSUME = "DCR_PG";
    private final static String HOST = "localhost";
    private final static int PORT = 30003;
    private float kwh;
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

            String pattern = "id=(\\d+)&totalKWH=(\\d.+)";

            Pattern regex = Pattern.compile(pattern);
            Matcher matcher = regex.matcher(message);

            if (matcher.find()) {
                id = Integer.parseInt(matcher.group(1));
                kwh = Float.parseFloat(matcher.group(2));
                System.out.println("Extracted id: " + id);
                System.out.println("Extracted kwh: " + kwh);
            } else {
                System.out.println("Numbers not found.");
            }
            if(kwh != 0.0)
                PDFGenerator.generate(getKwh(), Database.select(getId()));
            else
                System.out.println("Customer on this ID ("+id+") did not charge or does not exist yet!");
        };

        channel.basicConsume(CONSUME, true, deliverCallback, consumerTag -> {});
    }

    public float getKwh() {
        return kwh;
    }

    public int getId() {
        return id;
    }
}
