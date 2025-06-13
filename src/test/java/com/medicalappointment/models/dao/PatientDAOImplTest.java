package com.medicalappointment.models.dao;

import com.medicalappointment.models.Patient;
import com.medicalappointment.models.dao.exception.DAOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientDAOImplTest {
    @Mock
    private IDatabaseConnectionManager connectionManager;
    
    @Mock
    private Connection mockConnection;
    
    @Mock
    private PreparedStatement mockPreparedStatement;
    
    @Mock
    private Statement mockStatement;
    
    @Mock
    private ResultSet mockResultSet;
    
    private PatientDAOImpl patientDAO;
    private Patient testPatient;
    
    @BeforeEach
    void setUp() throws SQLException {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        
        // Initialize test data
        testPatient = new Patient(1, "Doe", "John", LocalDate.of(1990, 5, 15), "123 Main St", "555-123-4567");
        
        // Initialize DAO with mock connection manager
        patientDAO = new PatientDAOImpl(connectionManager);
        
        // Setup common mock behavior
        when(connectionManager.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockPreparedStatement);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }
    
    @Test
    void addPatient_ShouldInsertPatientAndSetId() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        ResultSet mockGeneratedKeys = mock(ResultSet.class);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockGeneratedKeys);
        when(mockGeneratedKeys.next()).thenReturn(true);
        when(mockGeneratedKeys.getInt(1)).thenReturn(5);
        
        // Act
        patientDAO.addPatient(testPatient);
        
        // Assert
        assertEquals(5, testPatient.getId()); // Verify ID was set from generated keys
        verify(mockPreparedStatement).setString(1, testPatient.getNom());
        verify(mockPreparedStatement).setString(2, testPatient.getPrenom());
        verify(mockPreparedStatement).setDate(3, Date.valueOf(testPatient.getDateNaissance()));
        verify(mockPreparedStatement).setString(4, testPatient.getAdresse());
        verify(mockPreparedStatement).setString(5, testPatient.getTelephone());
        verify(mockPreparedStatement).executeUpdate();
    }
    
    @Test
    void addPatient_WhenSQLExceptionOccurs_ShouldThrowDAOException() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));
        
        // Act & Assert
        Exception exception = assertThrows(
            DAOException.class,
            () -> patientDAO.addPatient(testPatient)
        );
        assertTrue(exception.getMessage().contains("Error adding patient"));
    }
    
    @Test
    void getPatientById_ShouldReturnPatientWhenFound() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("nom")).thenReturn("Doe");
        when(mockResultSet.getString("prenom")).thenReturn("John");
        when(mockResultSet.getDate("date_naissance")).thenReturn(Date.valueOf(LocalDate.of(1990, 5, 15)));
        when(mockResultSet.getString("adresse")).thenReturn("123 Main St");
        when(mockResultSet.getString("telephone")).thenReturn("555-123-4567");
        
        // Act
        Patient result = patientDAO.getPatientById(1);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Doe", result.getNom());
        assertEquals("John", result.getPrenom());
        assertEquals(LocalDate.of(1990, 5, 15), result.getDateNaissance());
        assertEquals("123 Main St", result.getAdresse());
        assertEquals("555-123-4567", result.getTelephone());
        verify(mockPreparedStatement).setInt(1, 1);
    }
    
    @Test
    void getPatientById_WhenPatientNotFound_ShouldReturnNull() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(false);
        
        // Act
        Patient result = patientDAO.getPatientById(99);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    void getAllPatients_ShouldReturnPatientList() throws SQLException {
        // Arrange
        // First call to next() returns true, second call returns false (to simulate one row in the result set)
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("nom")).thenReturn("Doe");
        when(mockResultSet.getString("prenom")).thenReturn("John");
        when(mockResultSet.getDate("date_naissance")).thenReturn(Date.valueOf(LocalDate.of(1990, 5, 15)));
        when(mockResultSet.getString("adresse")).thenReturn("123 Main St");
        when(mockResultSet.getString("telephone")).thenReturn("555-123-4567");
        
        // Act
        List<Patient> patients = patientDAO.getAllPatients();
        
        // Assert
        assertEquals(1, patients.size());
        Patient patient = patients.get(0);
        assertEquals(1, patient.getId());
        assertEquals("Doe", patient.getNom());
        assertEquals("John", patient.getPrenom());
    }
    
    @Test
    void updatePatient_ShouldExecuteUpdateStatement() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        
        // Act
        patientDAO.updatePatient(testPatient);
        
        // Assert
        verify(mockPreparedStatement).setString(1, testPatient.getNom());
        verify(mockPreparedStatement).setString(2, testPatient.getPrenom());
        verify(mockPreparedStatement).setDate(3, Date.valueOf(testPatient.getDateNaissance()));
        verify(mockPreparedStatement).setString(4, testPatient.getAdresse());
        verify(mockPreparedStatement).setString(5, testPatient.getTelephone());
        verify(mockPreparedStatement).setInt(6, testPatient.getId());
        verify(mockPreparedStatement).executeUpdate();
    }
    
    @Test
    void updatePatient_WhenSQLExceptionOccurs_ShouldThrowDAOException() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));
        
        // Act & Assert
        Exception exception = assertThrows(
            DAOException.class,
            () -> patientDAO.updatePatient(testPatient)
        );
        assertTrue(exception.getMessage().contains("Error updating patient"));
    }
    
    @Test
    void deletePatient_ShouldExecuteDeleteStatement() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        
        // Act
        patientDAO.deletePatient(1);
        
        // Assert
        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockPreparedStatement).executeUpdate();
    }
    
    @Test
    void deletePatient_WhenSQLExceptionOccurs_ShouldThrowDAOException() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));
        
        // Act & Assert
        Exception exception = assertThrows(
            DAOException.class,
            () -> patientDAO.deletePatient(1)
        );
        assertTrue(exception.getMessage().contains("Error deleting patient"));
    }
}
