package com.medicalappointment.controllers;

import com.medicalappointment.models.User;
import com.medicalappointment.service.IAuthenticationService;
import com.medicalappointment.service.exception.ServiceException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Label errorLabel;
    
    private IAuthenticationService authenticationService;
    private static User currentUser;

    // Constructor for dependency injection
    public LoginController(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    
    // Default constructor for FXMLLoader
    public LoginController() {
        try {
            com.medicalappointment.models.dao.IDatabaseConnectionManager dbManager = 
                com.medicalappointment.models.dao.DatabaseConnectionManager.getInstance();
            com.medicalappointment.models.dao.IUserDAO userDAO = 
                new com.medicalappointment.models.dao.UserDAOImpl(dbManager);
            this.authenticationService = 
                new com.medicalappointment.service.AuthenticationServiceImpl(userDAO);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error initializing login controller");
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        try {
            currentUser = authenticationService.authenticate(username, password);
            
            // Load main view after successful login
            Parent root = FXMLLoader.load(getClass().getResource("/com/medicalappointment/views/main_view.fxml"));            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/com/medicalappointment/css/style.css").toExternalForm());
            
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Medical Appointment Management");
            stage.show();
            
        } catch (ServiceException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred during login");
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    public static User getCurrentUser() {
        return currentUser;
    }
}
