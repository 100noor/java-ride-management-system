package com.mycompany.rideapp;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewTablesFrame extends JFrame {

    JTable table;
    DefaultTableModel model;

    public ViewTablesFrame() {
        setTitle("Database Tables");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        model = new DefaultTableModel();
        table = new JTable(model);
        add(new JScrollPane(table));

        loadUsersTable();   // you can change table here
    }

    private void loadUsersTable() {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM users")) {

            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            model.setRowCount(0);
            model.setColumnCount(0);

            for (int i = 1; i <= colCount; i++) {
                model.addColumn(meta.getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[colCount];
                for (int i = 1; i <= colCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
