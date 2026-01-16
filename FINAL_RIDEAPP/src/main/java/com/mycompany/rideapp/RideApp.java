package com.mycompany.rideapp;

public class RideApp {

    public static void main(String[] args) {
   
        DatabaseManager.initializeDatabase();

        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}