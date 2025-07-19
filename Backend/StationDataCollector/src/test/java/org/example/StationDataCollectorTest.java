package org.example;

import org.example.Service.Queue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StationDataCollectorTest {

    @Test
    public void getKWHTestNoSuccess() {  // Docker-Container muss laufen, damit das funktioniert, ID darf nicht existieren
        Queue queue = new Queue();

        float kwh = queue.getKWH("db_url=localhost:30011&id=6000");

        assertEquals(0, kwh);
    }

    @Test
    public void getKWHTestSuccessNoDocker() {
        Queue queue = mock(Queue.class);

        when(queue.getKWH("test")).thenReturn(Float.valueOf("22"));

        float kwh = queue.getKWH("test");

        assertEquals(22.0, kwh);
    }

    @Test
    public void getKWHTestSuccessWithDocker() { // Docker-Container muss laufen, damit das funktioniert, ID existiert und Daten haben sich nicht geändert seit 13.6.
        Queue queue = new Queue();

        float kwh = queue.getKWH("db_url=localhost:30012&id=2");
        String kwhString = String.format("%.2f", kwh);

        assertEquals("237,80", kwhString);
    }
}
