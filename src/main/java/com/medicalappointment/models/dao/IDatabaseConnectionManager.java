package com.medicalappointment.models.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDatabaseConnectionManager {
    Connection getConnection() throws SQLException;
    void closeConnection(Connection connection) throws SQLException;
}


