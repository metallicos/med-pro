package com.medicalappointment.service;

import com.medicalappointment.models.Medecin;
import com.medicalappointment.models.dao.IMedecinDAO;
import com.medicalappointment.service.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedecinServiceTest {
    @Mock
    private IMedecinDAO medecinDAO;
    
    private MedecinServiceImpl medecinService;
    
    private Medecin testMedecin;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        medecinService = new MedecinServiceImpl(medecinDAO);
        testMedecin = new Medecin(1, "Dr.", "Smith", "Cardiology", "MED123");
    }
    
    @Test
    void addMedecin_WithValidData_ShouldSucceed() {
        // Arrange
        doNothing().when(medecinDAO).addMedecin(any(Medecin.class));
        
        // Act
        medecinService.addMedecin(testMedecin);
        
        // Assert
        verify(medecinDAO).addMedecin(testMedecin);
    }
    
    @Test
    void addMedecin_WithNullName_ShouldThrowException() {
        // Arrange
        testMedecin.setNom(null);
        
        // Act & Assert
        ServiceException exception = assertThrows(
            ServiceException.class,
            () -> medecinService.addMedecin(testMedecin)
        );
        assertEquals("Medecin name cannot be empty.", exception.getMessage());
    }
    
    @Test
    void getAllMedecins_ShouldReturnList() {
        // Arrange
        List<Medecin> expectedMedecins = Arrays.asList(
            testMedecin,
            new Medecin(2, "Dr.", "Johnson", "Pediatrics", "MED456")
        );
        when(medecinDAO.getAllMedecins()).thenReturn(expectedMedecins);
        
        // Act
        List<Medecin> actualMedecins = medecinService.getAllMedecins();
        
        // Assert
        assertEquals(expectedMedecins, actualMedecins);
        verify(medecinDAO).getAllMedecins();
    }
    
    @Test
    void getMedecinById_WithValidId_ShouldReturnMedecin() {
        // Arrange
        when(medecinDAO.getMedecinById(1)).thenReturn(testMedecin);
        
        // Act
        Medecin result = medecinService.getMedecinById(1);
        
        // Assert
        assertEquals(testMedecin, result);
        verify(medecinDAO).getMedecinById(1);
    }
    
    @Test
    void getMedecinById_WithInvalidId_ShouldThrowException() {
        // Act & Assert
        ServiceException exception = assertThrows(
            ServiceException.class,
            () -> medecinService.getMedecinById(0)
        );
        assertEquals("Medecin ID must be positive.", exception.getMessage());
    }
    
    @Test
    void updateMedecin_WithValidData_ShouldSucceed() {
        // Arrange
        doNothing().when(medecinDAO).updateMedecin(any(Medecin.class));
        
        // Act
        medecinService.updateMedecin(testMedecin);
        
        // Assert
        verify(medecinDAO).updateMedecin(testMedecin);
    }
    
    @Test
    void deleteMedecin_WithValidId_ShouldSucceed() {
        // Arrange
        doNothing().when(medecinDAO).deleteMedecin(anyInt());
        
        // Act
        medecinService.deleteMedecin(1);
        
        // Assert
        verify(medecinDAO).deleteMedecin(1);
    }
}
