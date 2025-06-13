package com.medicalappointment.models.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager implements IDatabaseConnectionManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/medical_appointment_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Root123!"; // TODO: Use a more secure way to handle passwords

    public DatabaseConnectionManager() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.err.println("MySQL JDBC Driver not found.");
            throw new SQLException("MySQL JDBC Driver not found.", ex);
        }
    }

    public static DatabaseConnectionManager getInstance() throws SQLException {
        return new DatabaseConnectionManager();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    @Override
    public void closeConnection(Connection connection) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}


