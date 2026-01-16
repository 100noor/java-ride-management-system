package com.mycompany.rideapp;
import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    public RegisterFrame() {

       
        setTitle("Ride App - Registration");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

    
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 238, 220));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setContentPane(mainPanel);


        JLabel title = new JLabel("User Registration", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(75, 54, 33));
        mainPanel.add(title, BorderLayout.NORTH);

      
        JPanel formPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        formPanel.setBackground(new Color(245, 238, 220));

        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        JComboBox<String> role = new JComboBox<>(new String[]{"Customer", "Driver"});

        formPanel.add(new JLabel("Username"));
        formPanel.add(username);
        formPanel.add(new JLabel("Password"));
        formPanel.add(password);
        formPanel.add(new JLabel("Role"));
        formPanel.add(role);

        mainPanel.add(formPanel, BorderLayout.CENTER);

 
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(245, 238, 220));

        JButton registerBtn = new JButton("Register");
        styleButton(registerBtn, new Color(111, 78, 55));
        buttonPanel.add(registerBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        
        registerBtn.addActionListener(e -> {

            String u = username.getText().trim();
            String p = new String(password.getPassword());
            String r = role.getSelectedItem().toString();

            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields");
                return;
            }

            if (u.length() < 3) {
                JOptionPane.showMessageDialog(this, "Username must be at least 3 characters");
                return;
            }

            if (u.contains(" ")) {
                JOptionPane.showMessageDialog(this, "Username cannot contain spaces");
                return;
            }

            if (!u.matches("[a-zA-Z]+")) {
                JOptionPane.showMessageDialog(this, "Username can only contain letters");
                return;
            }

            if (p.length() < 3) {
                JOptionPane.showMessageDialog(this, "Password must be at least 3 characters");
                return;
            }

            if (p.contains(" ")) {
                JOptionPane.showMessageDialog(this, "Password cannot contain spaces");
                return;
            }

            if (!p.matches(".*\\d.*")) {
                JOptionPane.showMessageDialog(this, "Password must contain at least one number");
                return;
            }

            if (DatabaseManager.registerUser(u, p, r)) {
                JOptionPane.showMessageDialog(this,
                        "Registration Successful! Please Log in.");

                dispose();             
                new LoginFrame();        
            } else {
                JOptionPane.showMessageDialog(this,
                        "Registration failed. Try again.");
            }
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
