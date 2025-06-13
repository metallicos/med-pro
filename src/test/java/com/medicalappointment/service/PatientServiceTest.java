package com.medicalappointment.service;

import com.medicalappointment.models.Patient;
import com.medicalappointment.models.dao.IPatientDAO;
import com.medicalappointment.service.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceTest {
    @Mock
    private IPatientDAO patientDAO;
    
    private PatientServiceImpl patientService;
    
    private Patient testPatient;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        patientService = new PatientServiceImpl(patientDAO);
        // Initialize test patient with sample data
        testPatient = new Patient(1, "Doe", "John", LocalDate.of(1990, 5, 15), "123 Main St", "555-123-4567");
    }
    
    @Test
    void addPatient_WithValidData_ShouldSucceed() {
        // Arrange
        doNothing().when(patientDAO).addPatient(any(Patient.class));
        
        // Act
        patientService.addPatient(testPatient);
        
        // Assert
        verify(patientDAO).addPatient(testPatient);
    }
    
    @Test
    void addPatient_WithNullName_ShouldThrowException() {
        // Arrange
        testPatient.setNom(null);
        
        // Act & Assert
        ServiceException exception = assertThrows(
            ServiceException.class,
            () -> patientService.addPatient(testPatient)
        );
        assertEquals("Patient name cannot be empty.", exception.getMessage());
    }
    
    @Test
    void addPatient_WithNullSurname_ShouldThrowException() {
        // Arrange
        testPatient.setPrenom(null);
        
        // Act & Assert
        ServiceException exception = assertThrows(
            ServiceException.class,
            () -> patientService.addPatient(testPatient)
        );
        assertEquals("Patient surname cannot be empty.", exception.getMessage());
    }
    
    @Test
    void getAllPatients_ShouldReturnList() {
        // Arrange
        List<Patient> expectedPatients = Arrays.asList(
            testPatient,
            new Patient(2, "Smith", "Jane", LocalDate.of(1985, 3, 10), "456 Oak Ave", "555-987-6543")
        );
        when(patientDAO.getAllPatients()).thenReturn(expectedPatients);
        
        // Act
        List<Patient> actualPatients = patientService.getAllPatients();
        
        // Assert
        assertEquals(expectedPatients, actualPatients);
        verify(patientDAO).getAllPatients();
    }
    
    @Test
    void getPatientById_WithValidId_ShouldReturnPatient() {
        // Arrange
        when(patientDAO.getPatientById(1)).thenReturn(testPatient);
        
        // Act
        Patient result = patientService.getPatientById(1);
        
        // Assert
        assertEquals(testPatient, result);
        verify(patientDAO).getPatientById(1);
    }
    
    @Test
    void getPatientById_WithInvalidId_ShouldThrowException() {
        // Act & Assert
        ServiceException exception = assertThrows(
            ServiceException.class,
            () -> patientService.getPatientById(0)
        );
        assertEquals("Patient ID must be positive.", exception.getMessage());
    }
    
    @Test
    void updatePatient_WithValidData_ShouldSucceed() {
        // Arrange
        doNothing().when(patientDAO).updatePatient(any(Patient.class));
        
        // Act
        patientService.updatePatient(testPatient);
        
        // Assert
        verify(patientDAO).updatePatient(testPatient);
    }
    
    @Test
    void updatePatient_WithInvalidId_ShouldThrowException() {
        // Arrange
        testPatient.setId(0);
        
        // Act & Assert
        ServiceException exception = assertThrows(
            ServiceException.class,
            () -> patientService.updatePatient(testPatient)
        );
        assertEquals("Patient ID must be positive for update.", exception.getMessage());
    }
    
    @Test
    void updatePatient_WithNullName_ShouldThrowException() {
        // Arrange
        testPatient.setNom(null);
        
        // Act & Assert
        ServiceException exception = assertThrows(
            ServiceException.class,
            () -> patientService.updatePatient(testPatient)
        );
        assertEquals("Patient name cannot be empty.", exception.getMessage());
    }
    
    @Test
    void updatePatient_WithNullSurname_ShouldThrowException() {
        // Arrange
        testPatient.setPrenom(null);
        
        // Act & Assert
        ServiceException exception = assertThrows(
            ServiceException.class,
            () -> patientService.updatePatient(testPatient)
        );
        assertEquals("Patient surname cannot be empty.", exception.getMessage());
    }
    
    @Test
    void deletePatient_WithValidId_ShouldSucceed() {
        // Arrange
        doNothing().when(patientDAO).deletePatient(anyInt());
        
        // Act
        patientService.deletePatient(1);
        
        // Assert
        verify(patientDAO).deletePatient(1);
    }
    
    @Test
    void deletePatient_WithInvalidId_ShouldThrowException() {
        // Act & Assert
        ServiceException exception = assertThrows(
            ServiceException.class,
            () -> patientService.deletePatient(0)
        );
        assertEquals("Patient ID must be positive for deletion.", exception.getMessage());
    }
}
