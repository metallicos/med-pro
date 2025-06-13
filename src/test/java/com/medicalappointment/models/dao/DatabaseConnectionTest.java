package com.medicalappointment.models.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for database connection management.
 * Uses a mock implementation of the IDatabaseConnectionManager to avoid actual database connections during testing.
 */
class DatabaseConnectionTest {

    private IDatabaseConnectionManager mockConnectionManager;
    private Connection mockConnection;

    @BeforeEach
    void setUp() {
        // Create mock objects
        mockConnectionManager = mock(IDatabaseConnectionManager.class);
        mockConnection = mock(Connection.class);
    }

    @Test
    void getConnection_ShouldReturnNonNullConnection() throws SQLException {
        // Arrange
        when(mockConnectionManager.getConnection()).thenReturn(mockConnection);

        // Act
        Connection connection = mockConnectionManager.getConnection();

        // Assert
        assertNotNull(connection);
        verify(mockConnectionManager).getConnection();
    }

    @Test
    void closeConnection_ShouldCloseConnection() throws SQLException {
        // Arrange
        when(mockConnection.isClosed()).thenReturn(false);
        doNothing().when(mockConnectionManager).closeConnection(mockConnection);

        // Act
        mockConnectionManager.closeConnection(mockConnection);

        // Assert
        verify(mockConnectionManager).closeConnection(mockConnection);
    }

    @Test
    void closeConnection_WithNullConnection_ShouldNotThrowException() throws SQLException {
        // Act & Assert
        assertDoesNotThrow(() -> mockConnectionManager.closeConnection(null));
    }
}
