package com.medicalappointment.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class BaseController {
    // Store the initial window dimensions
    private static double lastWidth = 1024;  // Default width
    private static double lastHeight = 768;  // Default height
    private static boolean wasMaximized = false;
    private static double lastX = -1;
    private static double lastY = -1;

    protected void loadView(String fxmlPath, String title, Object source) {
        try {
            // Load the new view
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage;
            
            if (source instanceof Button) {
                stage = (Stage) ((Button) source).getScene().getWindow();
            } else if (source instanceof MenuItem) {
                stage = (Stage) ((MenuItem) source).getParentPopup().getOwnerWindow();
            } else {
                stage = (Stage) ((javafx.scene.Node) source).getScene().getWindow();
            }
            
            // Save current window state before changing scene
            if (!stage.isMaximized()) {
                lastWidth = stage.getWidth();
                lastHeight = stage.getHeight();
                lastX = stage.getX();
                lastY = stage.getY();
            }
            wasMaximized = stage.isMaximized();
            
            // Create and set the new scene
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/medicalappointment/css/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle(title);
            
            // Restore window state
            if (!wasMaximized) {
                if (lastX >= 0 && lastY >= 0) {
                    stage.setX(lastX);
                    stage.setY(lastY);
                }
                stage.setWidth(lastWidth);
                stage.setHeight(lastHeight);
            }
            stage.setMaximized(wasMaximized);
            
            // Add listeners to track window state changes
            stage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
                wasMaximized = newValue;
                if (!newValue) {
                    // Store current dimensions when leaving maximized state
                    lastWidth = stage.getWidth();
                    lastHeight = stage.getHeight();
                    lastX = stage.getX();
                    lastY = stage.getY();
                }
            });
            
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load view: " + e.getMessage());
        }
    }

    protected void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
