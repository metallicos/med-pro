package com.medicalappointment.controllers;

import com.medicalappointment.models.Medecin;
import com.medicalappointment.service.IMedecinService;
import com.medicalappointment.service.exception.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MedecinController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField specialiteField;
    @FXML
    private TextField numeroLicenceField;
    @FXML
    private TableView<Medecin> medecinTable;
    @FXML
    private TableColumn<Medecin, Integer> idColumn;
    @FXML
    private TableColumn<Medecin, String> nomColumn;
    @FXML
    private TableColumn<Medecin, String> prenomColumn;
    @FXML
    private TableColumn<Medecin, String> specialiteColumn;
    @FXML
    private TableColumn<Medecin, String> numeroLicenceColumn;

    private IMedecinService medecinService;
    private ObservableList<Medecin> medecinList;

    // Constructor for dependency injection
    public MedecinController(IMedecinService medecinService) {
        this.medecinService = medecinService;
    }

    // Default constructor for FXMLLoader
    public MedecinController() {
        try {
            this.medecinService = new com.medicalappointment.service.MedecinServiceImpl(
                new com.medicalappointment.models.dao.MedecinDAOImpl(
                    com.medicalappointment.models.dao.DatabaseConnectionManager.getInstance()
                )
            );
        } catch (Exception e) {
            e.printStackTrace();
            // Optionally, show an alert or log the error
        }
    }    @FXML
    public void initialize() {
        System.out.println("Initializing MedecinController...");
        
        // Initialize table columns with explicit cell value factories
        idColumn.setCellValueFactory(cellData -> {
            Integer id = cellData.getValue().getId();
            System.out.println("Setting ID cell value: " + id);
            return new javafx.beans.property.SimpleIntegerProperty(id).asObject();
        });
        
        nomColumn.setCellValueFactory(cellData -> {
            String nom = cellData.getValue().getNom();
            System.out.println("Setting Nom cell value: " + nom);
            return new javafx.beans.property.SimpleStringProperty(nom);
        });
        
        prenomColumn.setCellValueFactory(cellData -> {
            String prenom = cellData.getValue().getPrenom();
            System.out.println("Setting Prenom cell value: " + prenom);
            return new javafx.beans.property.SimpleStringProperty(prenom);
        });
        
        specialiteColumn.setCellValueFactory(cellData -> {
            String specialite = cellData.getValue().getSpecialite();
            System.out.println("Setting Specialite cell value: " + specialite);
            return new javafx.beans.property.SimpleStringProperty(specialite);
        });
        
        numeroLicenceColumn.setCellValueFactory(cellData -> {
            String numeroLicence = cellData.getValue().getNumeroLicence();
            System.out.println("Setting NumeroLicence cell value: " + numeroLicence);
            return new javafx.beans.property.SimpleStringProperty(numeroLicence);
        });

        medecinList = FXCollections.observableArrayList();
        medecinTable.setItems(medecinList);
        System.out.println("Table items set to observable list");

        loadMedecins();

        medecinTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showMedecinDetails(newValue));
        
        System.out.println("MedecinController initialization complete");
    }private void loadMedecins() {
        try {
            medecinList.clear();
            List<Medecin> medecins = medecinService.getAllMedecins();
            System.out.println("Loaded " + medecins.size() + " medecins from database");
            for (Medecin m : medecins) {
                System.out.println("Medecin: " + m.getId() + " - " + m.getNom() + " " + m.getPrenom());
            }
            medecinList.addAll(medecins);
            System.out.println("Observable list size: " + medecinList.size());
            
            // Force table refresh
            medecinTable.refresh();
        } catch (ServiceException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load medecins: " + e.getMessage());
        }
    }

    private void showMedecinDetails(Medecin medecin) {
        if (medecin != null) {
            nomField.setText(medecin.getNom());
            prenomField.setText(medecin.getPrenom());
            specialiteField.setText(medecin.getSpecialite());
            numeroLicenceField.setText(medecin.getNumeroLicence());
        } else {
            clearFields();
        }
    }    @FXML
    private void handleAddMedecin() {
        try {
            Medecin newMedecin = new Medecin(
                    0, // ID will be generated by DB
                    nomField.getText(),
                    prenomField.getText(),
                    specialiteField.getText(),
                    numeroLicenceField.getText()
            );
            System.out.println("Adding medecin: " + newMedecin.getNom() + " " + newMedecin.getPrenom());
            medecinService.addMedecin(newMedecin);
            System.out.println("Medecin added successfully with ID: " + newMedecin.getId());
            showAlert(Alert.AlertType.INFORMATION, "Success", "Medecin added successfully!");
            loadMedecins();
            clearFields();
        } catch (ServiceException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add medecin: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateMedecin() {
        Medecin selectedMedecin = medecinTable.getSelectionModel().getSelectedItem();
        if (selectedMedecin != null) {
            try {
                selectedMedecin.setNom(nomField.getText());
                selectedMedecin.setPrenom(prenomField.getText());
                selectedMedecin.setSpecialite(specialiteField.getText());
                selectedMedecin.setNumeroLicence(numeroLicenceField.getText());

                medecinService.updateMedecin(selectedMedecin);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Medecin updated successfully!");
                loadMedecins();
                clearFields();
            } catch (ServiceException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update medecin: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a medecin to update.");
        }
    }

    @FXML
    private void handleDeleteMedecin() {
        Medecin selectedMedecin = medecinTable.getSelectionModel().getSelectedItem();
        if (selectedMedecin != null) {
            try {
                medecinService.deleteMedecin(selectedMedecin.getId());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Medecin deleted successfully!");
                loadMedecins();
                clearFields();
            } catch (ServiceException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete medecin: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a medecin to delete.");
        }
    }

    @FXML
    private void handleClearFields() {
        clearFields();
    }

    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        specialiteField.clear();
        numeroLicenceField.clear();
        medecinTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBackToMain(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/medicalappointment/views/main_view.fxml"));
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/com/medicalappointment/css/style.css").toExternalForm());
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Medical Appointment Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to go back to main menu: " + e.getMessage());
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


