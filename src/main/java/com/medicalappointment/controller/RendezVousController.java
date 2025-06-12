package com.medicalappointment.controller;

import com.medicalappointment.model.Medecin;
import com.medicalappointment.model.Patient;
import com.medicalappointment.model.RendezVous;
import com.medicalappointment.service.IMedecinService;
import com.medicalappointment.service.IPatientService;
import com.medicalappointment.service.IRendezVousService;
import com.medicalappointment.service.exception.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class RendezVousController {

    @FXML
    private ComboBox<Patient> patientComboBox;
    @FXML
    private ComboBox<Medecin> medecinComboBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField timeField;
    @FXML
    private TextField motifField;
    @FXML
    private TableView<RendezVous> rendezVousTable;
    @FXML
    private TableColumn<RendezVous, Integer> idColumn;
    @FXML
    private TableColumn<RendezVous, String> patientColumn;
    @FXML
    private TableColumn<RendezVous, String> medecinColumn;
    @FXML
    private TableColumn<RendezVous, LocalDateTime> dateHeureColumn;
    @FXML
    private TableColumn<RendezVous, String> motifColumn;

    private IRendezVousService rendezVousService;
    private IPatientService patientService;
    private IMedecinService medecinService;
    private ObservableList<RendezVous> rendezVousList;

    // Constructor for dependency injection
    public RendezVousController(IRendezVousService rendezVousService, IPatientService patientService, IMedecinService medecinService) {
        this.rendezVousService = rendezVousService;
        this.patientService = patientService;
        this.medecinService = medecinService;
    }

    // Default constructor for FXMLLoader
    public RendezVousController() {
        try {
            com.medicalappointment.dao.IDatabaseConnectionManager dbManager = com.medicalappointment.dao.DatabaseConnectionManager.getInstance();
            com.medicalappointment.dao.IPatientDAO patientDAO = new com.medicalappointment.dao.PatientDAOImpl(dbManager);
            com.medicalappointment.dao.IMedecinDAO medecinDAO = new com.medicalappointment.dao.MedecinDAOImpl(dbManager);
            com.medicalappointment.dao.IRendezVousDAO rendezVousDAO = new com.medicalappointment.dao.RendezVousDAOImpl(dbManager, patientDAO, medecinDAO);
            com.medicalappointment.service.INotificationService notificationService = (message) -> {};
            this.rendezVousService = new com.medicalappointment.service.RendezVousServiceImpl(rendezVousDAO, notificationService);
            this.patientService = new com.medicalappointment.service.PatientServiceImpl(patientDAO);
            this.medecinService = new com.medicalappointment.service.MedecinServiceImpl(medecinDAO);
        } catch (Exception e) {
            e.printStackTrace();
            // Optionally, show an alert or log the error
        }
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        patientColumn.setCellValueFactory(cellData -> {
            Patient patient = cellData.getValue().getPatient();
            return new javafx.beans.property.SimpleStringProperty(patient.getNom() + " " + patient.getPrenom());
        });
        medecinColumn.setCellValueFactory(cellData -> {
            Medecin medecin = cellData.getValue().getMedecin();
            return new javafx.beans.property.SimpleStringProperty(medecin.getNom() + " " + medecin.getPrenom());
        });
        dateHeureColumn.setCellValueFactory(new PropertyValueFactory<>("dateHeure"));
        motifColumn.setCellValueFactory(new PropertyValueFactory<>("motif"));

        rendezVousList = FXCollections.observableArrayList();
        rendezVousTable.setItems(rendezVousList);

        loadPatientsAndMedecins();
        loadRendezVous();

        rendezVousTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showRendezVousDetails(newValue));
    }

    private void loadPatientsAndMedecins() {
        try {
            List<Patient> patients = patientService.getAllPatients();
            patientComboBox.setItems(FXCollections.observableArrayList(patients));
            List<Medecin> medecins = medecinService.getAllMedecins();
            medecinComboBox.setItems(FXCollections.observableArrayList(medecins));
        } catch (ServiceException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load patients or medecins: " + e.getMessage());
        }
    }

    private void loadRendezVous() {
        try {
            rendezVousList.clear();
            rendezVousList.addAll(rendezVousService.getAllRendezVous());
        } catch (ServiceException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load rendez-vous: " + e.getMessage());
        }
    }

    private void showRendezVousDetails(RendezVous rendezVous) {
        if (rendezVous != null) {
            patientComboBox.getSelectionModel().select(rendezVous.getPatient());
            medecinComboBox.getSelectionModel().select(rendezVous.getMedecin());
            datePicker.setValue(rendezVous.getDateHeure().toLocalDate());
            timeField.setText(rendezVous.getDateHeure().format(DateTimeFormatter.ofPattern("HH:mm")));
            motifField.setText(rendezVous.getMotif());
        } else {
            clearFields();
        }
    }

    @FXML
    private void handleAddRendezVous() {
        try {
            Patient selectedPatient = patientComboBox.getSelectionModel().getSelectedItem();
            Medecin selectedMedecin = medecinComboBox.getSelectionModel().getSelectedItem();
            LocalDate date = datePicker.getValue();
            String timeText = timeField.getText();
            String motif = motifField.getText();

            if (selectedPatient == null || selectedMedecin == null || date == null || timeText.isEmpty() || motif.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Missing Information", "Please fill in all fields.");
                return;
            }

            LocalTime time;
            try {
                time = LocalTime.parse(timeText, DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Time Format", "Please enter time in HH:MM format.");
                return;
            }

            LocalDateTime dateTime = LocalDateTime.of(date, time);

            RendezVous newRendezVous = new RendezVous(
                    0, // ID will be generated by DB
                    selectedPatient,
                    selectedMedecin,
                    dateTime,
                    motif
            );
            rendezVousService.addRendezVous(newRendezVous);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Rendez-vous added successfully!");
            loadRendezVous();
            clearFields();
        } catch (ServiceException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add rendez-vous: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateRendezVous() {
        RendezVous selectedRendezVous = rendezVousTable.getSelectionModel().getSelectedItem();
        if (selectedRendezVous != null) {
            try {
                Patient selectedPatient = patientComboBox.getSelectionModel().getSelectedItem();
                Medecin selectedMedecin = medecinComboBox.getSelectionModel().getSelectedItem();
                LocalDate date = datePicker.getValue();
                String timeText = timeField.getText();
                String motif = motifField.getText();

                if (selectedPatient == null || selectedMedecin == null || date == null || timeText.isEmpty() || motif.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Missing Information", "Please fill in all fields.");
                    return;
                }

                LocalTime time;
                try {
                    time = LocalTime.parse(timeText, DateTimeFormatter.ofPattern("HH:mm"));
                } catch (DateTimeParseException e) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Time Format", "Please enter time in HH:MM format.");
                    return;
                }

                LocalDateTime dateTime = LocalDateTime.of(date, time);

                selectedRendezVous.setPatient(selectedPatient);
                selectedRendezVous.setMedecin(selectedMedecin);
                selectedRendezVous.setDateHeure(dateTime);
                selectedRendezVous.setMotif(motif);

                rendezVousService.updateRendezVous(selectedRendezVous);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Rendez-vous updated successfully!");
                loadRendezVous();
                clearFields();
            } catch (ServiceException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update rendez-vous: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a rendez-vous to update.");
        }
    }

    @FXML
    private void handleDeleteRendezVous() {
        RendezVous selectedRendezVous = rendezVousTable.getSelectionModel().getSelectedItem();
        if (selectedRendezVous != null) {
            try {
                rendezVousService.deleteRendezVous(selectedRendezVous.getId());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Rendez-vous deleted successfully!");
                loadRendezVous();
                clearFields();
            } catch (ServiceException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete rendez-vous: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a rendez-vous to delete.");
        }
    }

    @FXML
    private void handleClearFields() {
        clearFields();
    }

    private void clearFields() {
        patientComboBox.getSelectionModel().clearSelection();
        medecinComboBox.getSelectionModel().clearSelection();
        datePicker.setValue(null);
        timeField.clear();
        motifField.clear();
        rendezVousTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBackToMain(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/medicalappointment/view/main_view.fxml"));
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/com/medicalappointment/view/style.css").toExternalForm());
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


