package org.example.Service;

import org.example.Data.Customer;

import java.sql.*;

public class Database {
    private final static String DRIVER = "postgresql";
    private final static String HOST = "localhost";
    private final static int PORT = 30001;
    private final static String DATABASE_NAME = "customerdb";
    private final static String USERNAME = "postgres";
    private final static String PASSWORD = "postgres";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getUrl());
    }

    public static Customer select(int sid) {

        String query = "SELECT * FROM customer WHERE id = ?;";

        Customer customer = null;
        try (
                Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setInt(1, sid);
            try (
                    ResultSet rs = ps.executeQuery()
            ) {
                while(rs.next()) {
                    int id = rs.getInt("id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");

                    if (id != 0 && !firstName.isEmpty() && !lastName.isEmpty()) {
                        customer = new Customer(id, firstName, lastName);
                    }

                    System.out.printf(customer.toString());
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return customer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getUrl() {
        //jdbc:DRIVER://HOST:PORT/DATABASE_NAME?username=USERNAME?password=PASSWORD
        return String.format("jdbc:%s://%s:%s/%s?user=%s&password=%s", DRIVER, HOST, PORT, DATABASE_NAME, USERNAME, PASSWORD);
    }
}
