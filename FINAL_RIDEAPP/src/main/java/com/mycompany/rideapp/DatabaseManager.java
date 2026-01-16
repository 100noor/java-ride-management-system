package com.mycompany.rideapp;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.PreparedStatement; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import javax.swing.JOptionPane;

public class DatabaseManager {

    private static final String CONNECTION_URL =
            "jdbc:sqlserver://localhost:56680;instanceName=SQLEXPRESS03;"
                    + "databaseName=RideManagement;encrypt=false;trustServerCertificate=true;"
                    + "user=alisha;password=123;";

    private static Connection connection = null;
       public static void initializeDatabase() { 
           try { Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
           connection = DriverManager.getConnection(CONNECTION_URL); 
           System.out.println("Database connection successful to RideManagement."); 
           } catch (ClassNotFoundException e) { 
               System.out.println("SQL Server JDBC Driver not found. Add the JAR file to your project."); 
               System.exit(1); } catch (SQLException e) { 
                   System.out.println("Error connecting to database: " + e.getMessage()); 
                   System.exit(1); } 
       }

    private static void ensureConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(CONNECTION_URL);
        }
    }
    public static boolean registerUser(String username, String password, String role) {

    String userSql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
    String driverSql = """
        INSERT INTO drivers
        (name, username, vehicle_brand, vehicle_capacity,
         fixed_destination, base_fare, monthly_salary)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

    try {
        ensureConnection();
        connection.setAutoCommit(false); // transaction start

        // 1️⃣ Insert into USERS
        PreparedStatement psUser = connection.prepareStatement(userSql);
        psUser.setString(1, username);
        psUser.setString(2, password);
        psUser.setString(3, role);
        psUser.executeUpdate();

        // 2️⃣ If DRIVER → insert into DRIVERS
        if (role.equalsIgnoreCase("Driver")) {

            PreparedStatement psDriver = connection.prepareStatement(driverSql);
            psDriver.setString(1, username);      // name
            psDriver.setString(2, username);      // username
            psDriver.setString(3, "Not Set");     // vehicle_brand
            psDriver.setInt(4, 4);                // vehicle_capacity
            psDriver.setString(5, "Not Set");     // fixed_destination
            psDriver.setInt(6, 0);                // base_fare
            psDriver.setInt(7, 0);                // monthly_salary
            psDriver.executeUpdate();
        }

        connection.commit(); // transaction success
        connection.setAutoCommit(true);

        return true;

    } catch (SQLException e) {
        try {
            connection.rollback(); // rollback on failure
        } catch (SQLException ignored) {}

        JOptionPane.showMessageDialog(null,
                "Registration failed:\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);

        return false;
    }
}

    public static boolean validateLogin(String username, String password, String role) 
    { String query = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ? AND role = ?"; 
    try { ensureConnection(); 
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) { 
        preparedStatement.setString(1, username); 
        preparedStatement.setString(2, password); 
        preparedStatement.setString(3, role); 
        try (ResultSet resultSet = preparedStatement.executeQuery()) { 
            if (resultSet.next()) { return resultSet.getInt(1) > 0; } } } 
    } catch (SQLException e) { 
        System.out.println("Login validation error: " + e.getMessage()); 
    } 
    return false; 
    }
    public static boolean bookRide(String username, String pickup, String destination, double fare) 
    { String sql = "INSERT INTO rides (customer_username, driver_id, pickup_location, destination, offered_fare, status) " 
            + "VALUES (?, (SELECT TOP 1 driver_id FROM drivers WHERE fixed_destination = ?), ?, ?, ?, 'Booked')"; 
    try { ensureConnection(); 
    try (PreparedStatement pst = connection.prepareStatement(sql)) { 
        pst.setString(1, username); pst.setString(2, destination); pst.setString(3, pickup); pst.setString(4, destination); pst.setDouble(5, fare); pst.executeUpdate(); 
        return true; 
    } 
    } catch (SQLException e) { 
        System.out.println("Booking Failed: " + e.getMessage()); 
        return false; 
    } 
    }

    public static DefaultTableModel getAvailableRides(String destination) {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Ride ID", "Customer", "Pickup", "Destination", "Fare"}, 0
        );

        String sql = "SELECT ride_id, customer_username, pickup_location, destination, offered_fare " +
                     "FROM rides WHERE status = 'Booked' AND destination = ?";

        try {
            ensureConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, destination);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("ride_id"),
                        rs.getString("customer_username"),
                        rs.getString("pickup_location"),
                        rs.getString("destination"),
                        rs.getDouble("offered_fare")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }
    public static DefaultTableModel getCustomerRides(String username) { 
        DefaultTableModel model = new DefaultTableModel(); 
        model.addColumn("ID"); 
        model.addColumn("Pickup"); 
        model.addColumn("Destination"); 
        model.addColumn("Fare"); 
        model.addColumn("Status"); 
        String sql = "SELECT ride_id, pickup_location, destination, offered_fare, status FROM rides WHERE customer_username = ?"; 
        try { ensureConnection(); 
        try (PreparedStatement pst = connection.prepareStatement(sql)) { 
            pst.setString(1, username); 
            try (ResultSet rs = pst.executeQuery()) { 
                while (rs.next()) { 
                    model.addRow(new Object[]{ 
                        rs.getInt("ride_id"), 
                        rs.getString("pickup_location"), 
                        rs.getString("destination"), 
                        rs.getDouble("offered_fare"), 
                        rs.getString("status") }); 
                } 
            } 
        } 
        } catch (SQLException e) { 
            e.printStackTrace(); 
        } 
        return model; 
    } 
    public static boolean updateRide(int rideId, String pickup, String dest, double fare) { 
        String sql = "UPDATE rides SET pickup_location = ?, destination = ?, offered_fare = ? WHERE ride_id = ?"; 
        try { ensureConnection(); 
        try (PreparedStatement pst = connection.prepareStatement(sql)) { 
            pst.setString(1, pickup); 
            pst.setString(2, dest); 
            pst.setDouble(3, fare); 
            pst.setInt(4, rideId); 
            return pst.executeUpdate() > 0; 
        } 
        } catch (SQLException e) { 
            System.out.println("Update Failed: " + e.getMessage()); 
            return false; 
        } 
    }
    public static boolean deleteRide(int rideId) { 
        String sql = "DELETE FROM rides WHERE ride_id = ?"; 
        try { ensureConnection(); 
        try (PreparedStatement pst = connection.prepareStatement(sql)) 
        { 
            pst.setInt(1, rideId); 
            return pst.executeUpdate() > 0; 
        } 
        } catch (SQLException e) { 
            System.out.println("Delete Failed: " + e.getMessage());
            return false; 
        } 
    }

    public static boolean acceptRide(int rideId, int driverId) {
        String sql = "UPDATE rides SET status='Accepted', driver_id=? WHERE ride_id=?";
        try {
            ensureConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, driverId);
            ps.setInt(2, rideId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean cancelRide(int rideId) {
        String sql = "UPDATE rides SET status='Cancelled', driver_id=NULL WHERE ride_id=?";
        try {
            ensureConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, rideId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static double getDriverTotalEarnings(int driverId) {
        String sql = "SELECT SUM(offered_fare) FROM rides WHERE driver_id=? AND status='Accepted'";
        try {
            ensureConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, driverId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static Driver getDriverByDestination(String destination) { 
        String query = "SELECT name, username, vehicle_brand, vehicle_capacity, base_fare FROM drivers WHERE fixed_destination = ?"; 
        try { ensureConnection(); 
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) { 
            preparedStatement.setString(1, destination); 
            try (ResultSet rs = preparedStatement.executeQuery()) { 
                if (rs.next()) { 
                    String name = rs.getString("name"); 
                    String brand = rs.getString("vehicle_brand"); 
                    int capacity = rs.getInt("vehicle_capacity"); 
                    int fare = (int) Math.round(rs.getDouble("base_fare")); 
                    return new Driver(name, new Car(brand, capacity), destination, fare); 
                } 
            } 
        } 
        } catch (SQLException e) { 
            System.out.println("Error fetching driver: " + e.getMessage()); 
        } 
        return new Driver("No Driver Found",
        new Car("N/A", 0), 
                destination, 0); 
    }
    
    public static Driver getDriverByusername(String username) {
        try {
            ensureConnection();
            String sql = "SELECT * FROM drivers WHERE username=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Vehicle v = new Vehicle(
                        rs.getString("vehicle_brand"),
                        rs.getInt("vehicle_capacity")
                );

                return new Driver(
                        rs.getInt("driver_id"),
                        rs.getString("username"),
                        v,
                        rs.getString("fixed_destination"),
                        (int) rs.getDouble("base_fare")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}



