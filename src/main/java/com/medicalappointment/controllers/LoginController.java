package com.medicalappointment.controllers;

import com.medicalappointment.models.User;
import com.medicalappointment.service.IAuthenticationService;
import com.medicalappointment.service.exception.ServiceException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController extends BaseController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    
    private IAuthenticationService authenticationService;
    private static User currentUser;
    
    public LoginController(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    
    public LoginController() {
    }
    
    public static User getCurrentUser() {
        return currentUser;
    }
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        try {
            currentUser = authenticationService.authenticate(username, password);
            loadView("/com/medicalappointment/views/main_view.fxml", "Medical Appointment Management", usernameField);
        } catch (ServiceException e) {
            showAlert(Alert.AlertType.ERROR, "Login Error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "System Error", "An error occurred during login");
        }
    }
}
