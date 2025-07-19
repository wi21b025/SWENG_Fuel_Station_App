package org.example;

import org.example.Service.Queue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataCollectionReceiverTest {

    @Test
    public void sendTotalTest(){
        Queue queue = new Queue();

        String total = queue.calculateTotal(List.of(
                "id=1&kwh=50.3",
                "id=1&kwh=37.8",
                "id=1&kwh=15.6"
        ));

        assertEquals("id=1&totalKWH=103.7", total);
    }

    @Test
    public void sendTotalTestNull(){
        Queue queue = new Queue();

        String total = queue.calculateTotal(
                List.of(
                        "id=&kwh=50.3",
                        "id=&kwh=37.8",
                        "id=&kwh=15.6"
                )
        );

        assertEquals(null, total);
    }
}
