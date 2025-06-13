package com.medicalappointment.service;

import com.medicalappointment.models.User;
import com.medicalappointment.models.dao.IUserDAO;
import com.medicalappointment.service.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {
    @Mock
    private IUserDAO userDAO;
    
    private AuthenticationServiceImpl authService;
    
    private User testUser;
      @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthenticationServiceImpl(userDAO);
        // Using the proper constructor to create a User
        testUser = new User("testuser", "password123", "USER");
        testUser.setId(1);
        testUser.setActive(true);
    }
    
    @Test
    void authenticate_WithValidCredentials_ShouldReturnUser() {
        // Arrange
        when(userDAO.getUserByUsername("testuser")).thenReturn(testUser);
        doNothing().when(userDAO).updateLastLogin(anyInt());
        
        // Act
        User authenticatedUser = authService.authenticate("testuser", "password123");
        
        // Assert
        assertNotNull(authenticatedUser);
        assertEquals(testUser, authenticatedUser);
        verify(userDAO).updateLastLogin(testUser.getId());
    }
    
    @Test
    void authenticate_WithInvalidUsername_ShouldThrowException() {
        // Arrange
        when(userDAO.getUserByUsername("invalid")).thenReturn(null);
        
        // Act & Assert
        ServiceException exception = assertThrows(
            ServiceException.class,
            () -> authService.authenticate("invalid", "password123")
        );
        assertEquals("Invalid username or password.", exception.getMessage());
    }
    
    @Test
    void authenticate_WithInvalidPassword_ShouldThrowException() {
        // Arrange
        when(userDAO.getUserByUsername("testuser")).thenReturn(testUser);
        
        // Act & Assert
        ServiceException exception = assertThrows(
            ServiceException.class,
            () -> authService.authenticate("testuser", "wrongpassword")
        );
        assertEquals("Invalid username or password.", exception.getMessage());
    }
    
    @Test
    void authenticate_WithInactiveUser_ShouldThrowException() {
        // Arrange
        testUser.setActive(false);
        when(userDAO.getUserByUsername("testuser")).thenReturn(testUser);
        
        // Act & Assert
        ServiceException exception = assertThrows(
            ServiceException.class,
            () -> authService.authenticate("testuser", "password123")
        );
        assertEquals("Invalid username or password.", exception.getMessage());
    }
    
    @Test
    void isAuthorized_WithAdminRole_ShouldReturnTrue() {
        // Arrange
        testUser.setRole("ADMIN");
        
        // Act
        boolean isAuthorized = authService.isAuthorized(testUser, "USER");
        
        // Assert
        assertTrue(isAuthorized);
    }
    
    @Test
    void isAuthorized_WithMatchingRole_ShouldReturnTrue() {
        // Act
        boolean isAuthorized = authService.isAuthorized(testUser, "USER");
        
        // Assert
        assertTrue(isAuthorized);
    }
    
    @Test
    void isAuthorized_WithNonMatchingRole_ShouldReturnFalse() {
        // Act
        boolean isAuthorized = authService.isAuthorized(testUser, "ADMIN");
        
        // Assert
        assertFalse(isAuthorized);
    }
}
