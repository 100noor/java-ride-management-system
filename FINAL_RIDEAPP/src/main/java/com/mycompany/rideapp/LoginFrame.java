package com.mycompany.rideapp;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    public static String currentUsername = null;

    public LoginFrame() {

        
        setTitle("Ride App - Login");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 238, 220));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setContentPane(mainPanel);
        JLabel title = new JLabel("Ride App Login", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(75, 54, 33));
        mainPanel.add(title, BorderLayout.NORTH);

     
        JPanel formPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        formPanel.setBackground(new Color(245, 238, 220));

        JComboBox<String> role = new JComboBox<>(new String[]{"Customer", "Driver"});
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();

        formPanel.add(new JLabel("Select Role"));
        formPanel.add(role);
        formPanel.add(new JLabel("Username"));
        formPanel.add(username);
        formPanel.add(new JLabel("Password"));
        formPanel.add(password);

        mainPanel.add(formPanel, BorderLayout.CENTER);

     
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(245, 238, 220));

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        styleButton(loginBtn, new Color(111, 78, 55));
        styleButton(registerBtn, new Color(70, 130, 180));

        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        
        loginBtn.addActionListener(e -> {

            String u = username.getText().trim();
            String p = new String(password.getPassword());
            String r = role.getSelectedItem().toString();

            if (u.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username is required");
                return;
            }

            if (p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password is required");
                return;
            }

            if (DatabaseManager.validateLogin(u, p, r)) {
                currentUsername = u;

                if (r.equals("Customer")) {
                    new CustomerFrame();
                } else {
                    new DriverFrame();
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Login!");
            }
        });

   
        registerBtn.addActionListener(e -> {
            new RegisterFrame();
            dispose();
        });

        setVisible(true);
    }

   
    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }
}
