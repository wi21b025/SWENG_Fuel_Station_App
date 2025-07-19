package org.example.Service;

import org.example.Data.Station;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private final static String DRIVER = "postgresql";
    private final static String HOST = "localhost";
    private final static int PORT = 30002;
    private final static String DATABASE_NAME = "stationdb";
    private final static String USERNAME = "postgres";
    private final static String PASSWORD = "postgres";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getUrl());
    }

    public static List<Station> select() {

        String query = "SELECT * FROM station";

        List<Station> stations = new ArrayList<>();
        try (
                Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery()
        ) {
            while(rs.next()) {
                int id = rs.getInt("id");
                String db_url = rs.getString("db_url");
                float lat = rs.getFloat("lat");
                float lng = rs.getFloat("lng");

                Station station = new Station(id, db_url, lat, lng);
                stations.add(station);

                System.out.printf(station.toString());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return stations;
    }

    private static String getUrl() {
        //jdbc:DRIVER://HOST:PORT/DATABASE_NAME?username=USERNAME?password=PASSWORD
        return String.format(
                "jdbc:%s://%s:%s/%s?user=%s&password=%s", DRIVER, HOST, PORT, DATABASE_NAME, USERNAME, PASSWORD
        );
    }
}
