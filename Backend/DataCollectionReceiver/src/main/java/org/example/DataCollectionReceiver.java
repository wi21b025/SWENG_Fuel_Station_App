package org.example;

import org.example.Service.Queue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DataCollectionReceiver {
    private final Queue queue;


    public DataCollectionReceiver(Queue queue) {
        this.queue = queue;
    }

    public void waitForData() throws IOException, TimeoutException {
        this.queue.consumeExpectedMessages();
        this.queue.consumeActualMessages();
    }
}
