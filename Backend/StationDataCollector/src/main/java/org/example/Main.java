package org.example;

import org.example.Service.Queue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, TimeoutException {
        Queue q1 = new Queue();
        q1.receive();
    }
}