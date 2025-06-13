package com.medicalappointment.controllers;

import com.medicalappointment.models.User;
import com.medicalappointment.service.IAuthenticationService;
import com.medicalappointment.service.exception.ServiceException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController extends BaseController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    
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
}
