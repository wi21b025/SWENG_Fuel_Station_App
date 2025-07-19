package org.example;

import org.example.Service.Queue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, TimeoutException {
        Queue queue = new Queue();
        DataCollectionReceiver dcr = new DataCollectionReceiver(queue);
        dcr.waitForData();
    }
}