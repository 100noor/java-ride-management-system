package com.mycompany.rideapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DriverDAO {

    // CREATE
    public static boolean addDriver(String name, String username, String brand,
                                    int capacity, String destination,
                                    double fare, double salary) {

        String sql = "INSERT INTO drivers VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, username);
            ps.setString(3, brand);
            ps.setInt(4, capacity);
            ps.setString(5, destination);
            ps.setDouble(6, fare);
            ps.setDouble(7, salary);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ
    public static ResultSet getDriverByDestination(String destination) {
        String sql = "SELECT * FROM drivers WHERE fixed_destination=?";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, destination);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    
    public static boolean updateSalary(String username, double salary) {
        String sql = "UPDATE drivers SET monthly_salary=? WHERE username=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, salary);
            ps.setString(2, username);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean deleteDriver(String username) {
        String sql = "DELETE FROM drivers WHERE username=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
