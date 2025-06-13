package com.medicalappointment.service;

import com.medicalappointment.models.Medecin;
import com.medicalappointment.models.Patient;
import com.medicalappointment.models.RendezVous;
import com.medicalappointment.models.dao.IRendezVousDAO;
import com.medicalappointment.service.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RendezVousServiceTest {
    @Mock
    private IRendezVousDAO rendezVousDAO;
    
    @Mock
    private INotificationService notificationService;
    
    private RendezVousServiceImpl rendezVousService;
    
    private Patient testPatient;
    private Medecin testMedecin;
    private RendezVous testRendezVous;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rendezVousService = new RendezVousServiceImpl(rendezVousDAO, notificationService);
        
        testPatient = new Patient(1, "John", "Doe", LocalDateTime.now().toLocalDate(), "123 Street", "123456789");
        testMedecin = new Medecin(1, "Dr.", "Smith", "Cardiology", "MED123");
        testRendezVous = new RendezVous(1, testPatient, testMedecin, LocalDateTime.now(), "Checkup");
    }
    
    @Test
    void addRendezVous_WithValidData_ShouldSucceed() {
        // Arrange
        doNothing().when(rendezVousDAO).addRendezVous(any(RendezVous.class));
        
        // Act
        rendezVousService.addRendezVous(testRendezVous);
        
        // Assert
        verify(rendezVousDAO).addRendezVous(testRendezVous);
        verify(notificationService).sendReminder(testRendezVous);
    }
    
    @Test
    void addRendezVous_WithNullPatient_ShouldThrowException() {
        // Arrange
        testRendezVous.setPatient(null);
        
        // Act & Assert
        ServiceException exception = assertThrows(
            ServiceException.class,
            () -> rendezVousService.addRendezVous(testRendezVous)
        );
        assertEquals("RendezVous must have a patient.", exception.getMessage());
    }
    
    @Test
    void addRendezVous_WithNullMedecin_ShouldThrowException() {
        // Arrange
        testRendezVous.setMedecin(null);
        
        // Act & Assert
        ServiceException exception = assertThrows(
            ServiceException.class,
            () -> rendezVousService.addRendezVous(testRendezVous)
        );
        assertEquals("RendezVous must have a medecin.", exception.getMessage());
    }
    
    @Test
    void updateRendezVous_WithValidData_ShouldSucceed() {
        // Arrange
        doNothing().when(rendezVousDAO).updateRendezVous(any(RendezVous.class));
        
        // Act
        rendezVousService.updateRendezVous(testRendezVous);
        
        // Assert
        verify(rendezVousDAO).updateRendezVous(testRendezVous);
        verify(notificationService).sendReminder(testRendezVous);
    }
    
    @Test
    void deleteRendezVous_WithValidId_ShouldSucceed() {
        // Arrange
        doNothing().when(rendezVousDAO).deleteRendezVous(anyInt());
        
        // Act
        rendezVousService.deleteRendezVous(1);
        
        // Assert
        verify(rendezVousDAO).deleteRendezVous(1);
    }
    
    @Test
    void deleteRendezVous_WithInvalidId_ShouldThrowException() {
        // Act & Assert
        ServiceException exception = assertThrows(
            ServiceException.class,
            () -> rendezVousService.deleteRendezVous(0)
        );
        assertEquals("RendezVous ID must be positive for deletion.", exception.getMessage());
    }
}
