package com.medicalappointment.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

import java.io.IOException;

public class MainController {

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Clear the current user
            LoginController.getCurrentUser().setActive(false);

            // Load login view
            Parent root = FXMLLoader.load(getClass().getResource("/com/medicalappointment/views/login_view.fxml"));
            Scene scene = new Scene(root, 400, 500);
            scene.getStylesheets().add(getClass().getResource("/com/medicalappointment/views/style.css").toExternalForm());

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("MediCare Pro Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to logout: " + e.getMessage());
        }
    }

    @FXML
    private void handleManagePatients(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/medicalappointment/views/patient_view.fxml"));
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/com/medicalappointment/views/style.css").toExternalForm());
            Stage stage = (Stage) ((event.getSource() instanceof Button)
                ? ((Button) event.getSource()).getScene().getWindow()
                : ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow());
            stage.setScene(scene);
            stage.setTitle("Manage Patients");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load patient view: " + e.getMessage());
        }
    }

    @FXML
    private void handleManageMedecins(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/medicalappointment/views/medecin_view.fxml"));
            Stage stage = (Stage) ((event.getSource() instanceof Button)
                ? ((Button) event.getSource()).getScene().getWindow()
                : ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow());
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Manage Medecins");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load medecin view: " + e.getMessage());
        }
    }

    @FXML
    private void handleManageRendezVous(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/medicalappointment/views/rendezvous_view.fxml"));
            Stage stage = (Stage) ((event.getSource() instanceof Button)
                ? ((Button) event.getSource()).getScene().getWindow()
                : ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow());
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Manage Rendez-vous");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load rendez-vous view: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


