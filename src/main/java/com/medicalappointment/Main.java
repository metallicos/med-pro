package com.medicalappointment;

import com.medicalappointment.controllers.ControllerFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        // Get screen dimensions
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getVisualBounds().getWidth();
        double screenHeight = screen.getVisualBounds().getHeight();
        
        // Set initial window dimensions
        double initialWidth = screenWidth * 0.8;  // 80% of screen width
        double initialHeight = screenHeight * 0.8;  // 80% of screen height

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/medicalappointment/views/login_view.fxml"));
        loader.setControllerFactory(new ControllerFactory());
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/medicalappointment/css/style.css").toExternalForm());
        
        primaryStage.setTitle("MediCare Pro Login");
        primaryStage.setScene(scene);
        
        // Set initial size before maximizing
        primaryStage.setWidth(initialWidth);
        primaryStage.setHeight(initialHeight);
        
        // Center on screen
        primaryStage.centerOnScreen();
        
        // Maximize window
        primaryStage.setMaximized(true);
        
        // Add state change listener
        primaryStage.maximizedProperty().addListener((obs, wasMaximized, isNowMaximized) -> {
            if (!isNowMaximized) {
                // When un-maximizing, set to initial dimensions and center
                primaryStage.setWidth(initialWidth);
                primaryStage.setHeight(initialHeight);
                primaryStage.centerOnScreen();
            }
        });
        
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


