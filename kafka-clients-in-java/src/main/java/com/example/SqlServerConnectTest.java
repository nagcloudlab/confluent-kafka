package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlServerConnectTest {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=TestDB;encrypt=false";
        String user = "sa";
        String password = "YourStrong@Passw0rd";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database!");

            // get all ros from Employees table
            String query = "SELECT * FROM Employees";
            try (var stmt = conn.createStatement();
                 var rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    System.out.println(rs.getInt("Id") + ", " + rs.getString("Name"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
