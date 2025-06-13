package com.medicalappointment.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class MainController extends BaseController {
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Clear the current user
            LoginController.getCurrentUser().setActive(false);
            loadView("/com/medicalappointment/views/login_view.fxml", "MediCare Pro Login", event.getSource());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to logout: " + e.getMessage());
        }
    }

    @FXML
    private void handleManagePatients(ActionEvent event) {
        loadView("/com/medicalappointment/views/patient_view.fxml", "Manage Patients", event.getSource());
    }

    @FXML
    private void handleManageMedecins(ActionEvent event) {
        loadView("/com/medicalappointment/views/medecin_view.fxml", "Manage Medecins", event.getSource());
    }

    @FXML
    private void handleManageRendezVous(ActionEvent event) {
        loadView("/com/medicalappointment/views/rendezvous_view.fxml", "Manage Rendez-vous", event.getSource());
    }
}


