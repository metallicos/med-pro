package com.medicalappointment;

import com.medicalappointment.controllers.ControllerFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/medicalappointment/views/login_view.fxml"));
        loader.setControllerFactory(new ControllerFactory());
        Parent root = loader.load();

        Scene scene = new Scene(root, 400, 500);
        scene.getStylesheets().add(getClass().getResource("/com/medicalappointment/css/style.css").toExternalForm());
        primaryStage.setTitle("MediCare Pro Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


