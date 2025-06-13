package com.medicalappointment.controllers;

import com.medicalappointment.models.Medecin;
import com.medicalappointment.models.Patient;
import com.medicalappointment.models.RendezVous;
import com.medicalappointment.service.IMedecinService;
import com.medicalappointment.service.IPatientService;
import com.medicalappointment.service.IRendezVousService;
import com.medicalappointment.service.exception.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class RendezVousController extends BaseController {

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
            com.medicalappointment.models.dao.IDatabaseConnectionManager dbManager = com.medicalappointment.models.dao.DatabaseConnectionManager.getInstance();
            com.medicalappointment.models.dao.IPatientDAO patientDAO = new com.medicalappointment.models.dao.PatientDAOImpl(dbManager);
            com.medicalappointment.models.dao.IMedecinDAO medecinDAO = new com.medicalappointment.models.dao.MedecinDAOImpl(dbManager);
            com.medicalappointment.models.dao.IRendezVousDAO rendezVousDAO = new com.medicalappointment.models.dao.RendezVousDAOImpl(dbManager, patientDAO, medecinDAO);
            com.medicalappointment.service.INotificationService notificationService = (message) -> {};
            this.rendezVousService = new com.medicalappointment.service.RendezVousServiceImpl(rendezVousDAO, notificationService);
            this.patientService = new com.medicalappointment.service.PatientServiceImpl(patientDAO);
            this.medecinService = new com.medicalappointment.service.MedecinServiceImpl(medecinDAO);
        } catch (Exception e) {
            e.printStackTrace();
            // Optionally, show an alert or log the error
        }
    }    @FXML
    public void initialize() {
        System.out.println("Initializing RendezVousController...");
        
        idColumn.setCellValueFactory(cellData -> {
            Integer id = cellData.getValue().getId();
            System.out.println("Setting ID cell value: " + id);
            return new javafx.beans.property.SimpleIntegerProperty(id).asObject();
        });
        
        patientColumn.setCellValueFactory(cellData -> {
            Patient patient = cellData.getValue().getPatient();
            String patientName = patient.getNom() + " " + patient.getPrenom();
            System.out.println("Setting Patient cell value: " + patientName);
            return new javafx.beans.property.SimpleStringProperty(patientName);
        });
        
        medecinColumn.setCellValueFactory(cellData -> {
            Medecin medecin = cellData.getValue().getMedecin();
            String medecinName = medecin.getNom() + " " + medecin.getPrenom();
            System.out.println("Setting Medecin cell value: " + medecinName);
            return new javafx.beans.property.SimpleStringProperty(medecinName);
        });
        
        dateHeureColumn.setCellValueFactory(cellData -> {
            java.time.LocalDateTime dateHeure = cellData.getValue().getDateHeure();
            System.out.println("Setting DateHeure cell value: " + dateHeure);
            return new javafx.beans.property.SimpleObjectProperty<>(dateHeure);
        });
        
        motifColumn.setCellValueFactory(cellData -> {
            String motif = cellData.getValue().getMotif();
            System.out.println("Setting Motif cell value: " + motif);
            return new javafx.beans.property.SimpleStringProperty(motif);
        });

        rendezVousList = FXCollections.observableArrayList();
        rendezVousTable.setItems(rendezVousList);
        System.out.println("Table items set to observable list");

        loadPatientsAndMedecins();
        loadRendezVous();

        rendezVousTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showRendezVousDetails(newValue));
        
        System.out.println("RendezVousController initialization complete");
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
    }    private void loadRendezVous() {
        try {
            rendezVousList.clear();
            List<RendezVous> rendezVous = rendezVousService.getAllRendezVous();
            System.out.println("Loaded " + rendezVous.size() + " rendez-vous from database");
            for (RendezVous rv : rendezVous) {
                System.out.println("RendezVous: " + rv.getId() + " - " + rv.getPatient().getNom() + " with " + rv.getMedecin().getNom());
            }
            rendezVousList.addAll(rendezVous);
            System.out.println("Observable list size: " + rendezVousList.size());
            
            // Force table refresh
            rendezVousTable.refresh();
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

            LocalDateTime dateTime = LocalDateTime.of(date, time);            RendezVous newRendezVous = new RendezVous(
                    0, // ID will be generated by DB
                    selectedPatient,
                    selectedMedecin,
                    dateTime,
                    motif
            );
            System.out.println("Adding rendez-vous: " + selectedPatient.getNom() + " with " + selectedMedecin.getNom());
            rendezVousService.addRendezVous(newRendezVous);
            System.out.println("RendezVous added successfully with ID: " + newRendezVous.getId());
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
        loadView("/com/medicalappointment/views/main_view.fxml", "Medical Appointment Management", event.getSource());
    }    @Override
    protected void showAlert(Alert.AlertType alertType, String title, String message) {
        super.showAlert(alertType, title, message);
    }
}


