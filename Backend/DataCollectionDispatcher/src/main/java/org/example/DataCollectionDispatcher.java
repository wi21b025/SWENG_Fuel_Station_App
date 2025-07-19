package org.example;

import org.example.Data.Station;
import org.example.Service.Database;
import org.example.Service.Queue;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class DataCollectionDispatcher {

    private final Database database;
    private final Queue queue;


    public DataCollectionDispatcher(Database database, Queue queue) {
        this.database = database;
        this.queue = queue;
    }

    public List<Station> getDatabase() {
        return Database.select();
    }

    public void wait(List<Station> stations) throws IOException, TimeoutException {
        this.queue.receive(stations);
    }
}
