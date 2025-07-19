package org.example;

import com.rabbitmq.client.ConnectionFactory;
import org.example.Data.Station;
import org.example.Service.Database;
import org.example.Service.Queue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataCollectionDispatcherTest {
    @Test
    public void DCDTest() {
        DataCollectionDispatcher dcd = mock(DataCollectionDispatcher.class);

        when(dcd.getDatabase()).thenReturn(
                List.of(
                        new Station(1, "localhost:1001", 50.0000F, 50.0000F),
                        new Station(1, "localhost:1002", 50.0000F, 50.0000F),
                        new Station(1, "localhost:1003", 50.0000F, 50.0000F),
                        new Station(1, "localhost:1004", 50.0000F, 50.0000F)
                )
        );

        int stations = dcd.getDatabase().size();

        assertEquals(4, stations);
    }
}
