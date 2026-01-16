package com.mycompany.rideapp;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:sqlserver://localhost;instanceName=SQLEXPRESS03;" +
                    "databaseName=RideManagement;encrypt=false;trustServerCertificate=true;";

    private static final String USER = "alisha";
    private static final String PASS = "123";

    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println("Connecting to: " + URL);
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("✅ SUCCESS! Connected to SQL Server via Instance Name.");
        } else {
            System.out.println("❌ FAILURE. Make sure 'SQL Server Browser' service is running.");
        }
    }
}