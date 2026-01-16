package com.mycompany.rideapp;

import javax.swing.*;
import java.awt.*;

public class DriverFrame extends JFrame {

    Driver driver;
    JTable rideTable;
    JLabel earningsLabel;

    public DriverFrame() {

        driver = DatabaseManager.getDriverByusername(LoginFrame.currentUsername);

        setTitle("Driver Dashboard");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 238, 220));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        setContentPane(mainPanel);

        JLabel title = new JLabel("Driver Dashboard", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(75, 54, 33));
        mainPanel.add(title, BorderLayout.NORTH);

        // Ride Table
        rideTable = new JTable(DatabaseManager.getAvailableRides(driver.destination));
        JScrollPane scrollPane = new JScrollPane(rideTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(245, 238, 220));

        earningsLabel = new JLabel();
        updateEarnings();

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(245, 238, 220));

        JButton acceptBtn = new JButton("Accept Ride");
        JButton cancelBtn = new JButton("Cancel Ride");
        JButton logoutBtn = new JButton("Logout");

        styleButton(acceptBtn, new Color(34, 139, 34));
        styleButton(cancelBtn, new Color(178, 34, 34));
        styleButton(logoutBtn, new Color(70, 130, 180));

        btnPanel.add(acceptBtn);
        btnPanel.add(cancelBtn);
        btnPanel.add(logoutBtn);

        bottomPanel.add(earningsLabel, BorderLayout.NORTH);
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        acceptBtn.addActionListener(e -> handleAccept());
        cancelBtn.addActionListener(e -> handleCancel());
        logoutBtn.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        setVisible(true);
    }

    private void handleAccept() {
        int row = rideTable.getSelectedRow();
        if (row == -1) return;

        int rideId = (int) rideTable.getValueAt(row, 0);
        if (DatabaseManager.acceptRide(rideId, driver.driverId)) {
            refreshTable();
            updateEarnings();
        }
    }

    private void handleCancel() {
        int row = rideTable.getSelectedRow();
        if (row == -1) return;

        int rideId = (int) rideTable.getValueAt(row, 0);
        if (DatabaseManager.cancelRide(rideId)) {
            refreshTable();
        }
    }

    private void refreshTable() {
        rideTable.setModel(DatabaseManager.getAvailableRides(driver.destination));
    }

    private void updateEarnings() {
        double total = DatabaseManager.getDriverTotalEarnings(driver.driverId);
        earningsLabel.setText("Total Earnings: Rs. " + total);
        earningsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }
}

