package com.mycompany.rideapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;

public class CustomerFrame extends JFrame {

    JTable rideTable;
    JTextField location, fareField;
    JComboBox<String> destinations;
    public CustomerFrame() {

        
        setTitle("Customer Panel - " + LoginFrame.currentUsername);
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

      
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 238, 220));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        
        JLabel title = new JLabel("Customer Ride Panel", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(75, 54, 33));
        mainPanel.add(title, BorderLayout.NORTH);

      
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBackground(new Color(245, 238, 220));

        location = new JTextField();
        destinations = new JComboBox<>(new String[]{"Airport", "City Mall", "Railway Station"});
        fareField = new JTextField();

        inputPanel.add(new JLabel("Pickup Location:"));
        inputPanel.add(location);
        inputPanel.add(new JLabel("Destination:"));
        inputPanel.add(destinations);
        inputPanel.add(new JLabel("Offer Fare:"));
        inputPanel.add(fareField);

        JButton bookBtn = new JButton("Book Ride");
        JButton updateBtn = new JButton("Update Ride");

        styleButton(bookBtn, new Color(111, 78, 55));
        styleButton(updateBtn, new Color(70, 130, 180));

        inputPanel.add(bookBtn);
        inputPanel.add(updateBtn);

        mainPanel.add(inputPanel, BorderLayout.WEST);

     
        rideTable = new JTable();
        rideTable.setRowHeight(25);
        rideTable.setSelectionBackground(new Color(180, 160, 120));
        rideTable.setGridColor(new Color(150, 130, 100));

        JScrollPane scrollPane = new JScrollPane(rideTable);
        scrollPane.setPreferredSize(new Dimension(450, 300));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

       
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottomPanel.setBackground(new Color(245, 238, 220));

        JButton deleteBtn = new JButton("Cancel Ride");
        JButton logoutBtn = new JButton("Logout");

        styleButton(deleteBtn, new Color(178, 34, 34));
        styleButton(logoutBtn, new Color(105, 105, 105));

        bottomPanel.add(deleteBtn);
        bottomPanel.add(logoutBtn);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

      
        loadTableData();

     
        rideTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = rideTable.getSelectedRow();
                if (row != -1) {
                    location.setText(rideTable.getValueAt(row, 1).toString());
                    destinations.setSelectedItem(rideTable.getValueAt(row, 2).toString());
                    double fare = Double.parseDouble(
                           rideTable.getValueAt(row, 3).toString()
                    );
                    
                    fareField.setText("Rs " + fare);  
                }
            }
        });

        bookBtn.addActionListener(e -> {
            String pickup = location.getText().trim();
            String dest = destinations.getSelectedItem().toString();

            if (pickup.isEmpty() || pickup.length() < 3 || !pickup.matches("[a-zA-Z ,]+")) {
                JOptionPane.showMessageDialog(this, "Invalid pickup location.");
                return;
            }

            try {
                double fare = Double.parseDouble(fareField.getText().trim());

                if (fare < 300) {
                    JOptionPane.showMessageDialog(this, "Minimum fare is 300.");
                    return;
                }

                if (DatabaseManager.bookRide(LoginFrame.currentUsername, pickup, dest, fare)) {
                    JOptionPane.showMessageDialog(this, "Ride Booked!");
                    loadTableData();
                    clearFields();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid fare.");
            }
        });

        
        updateBtn.addActionListener(e -> {
            int row = rideTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a ride first.");
                return;
            }

            int rideId = Integer.parseInt(rideTable.getValueAt(row, 0).toString());

            try {
                double fare = Double.parseDouble(fareField.getText().trim());
                if (fare < 300) {
                    JOptionPane.showMessageDialog(this, "Minimum fare is 300.");
                    return;
                }

                if (DatabaseManager.updateRide(rideId, location.getText(), destinations.getSelectedItem().toString(), fare)) {
                    JOptionPane.showMessageDialog(this, "Ride Updated!");
                    loadTableData();
                    clearFields();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input.");
            }
        });

      
        deleteBtn.addActionListener(e -> {
            int row = rideTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a ride.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Cancel this ride?");
            if (confirm == JOptionPane.YES_OPTION) {
                int rideId = Integer.parseInt(rideTable.getValueAt(row, 0).toString());
                DatabaseManager.deleteRide(rideId);
                loadTableData();
                clearFields();
            }
        });

      
        logoutBtn.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        setVisible(true);
    }

   
    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }

    private void loadTableData() {
        DefaultTableModel model = DatabaseManager.getCustomerRides(LoginFrame.currentUsername);
        rideTable.setModel(model);
    }

    private void clearFields() {
        location.setText("");
        fareField.setText("");
    }
}