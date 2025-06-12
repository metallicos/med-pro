package com.medicalappointment.controller;

import com.medicalappointment.dao.*;
import com.medicalappointment.service.*;
import javafx.util.Callback;
import java.sql.SQLException;

public class ControllerFactory implements Callback<Class<?>, Object> {

    private IPatientService patientService;
    private IMedecinService medecinService;
    private IRendezVousService rendezVousService;
    private INotificationService notificationService;
    private IAuthenticationService authenticationService;

    public ControllerFactory() {
        try {
            DatabaseConnectionManager connectionManager = DatabaseConnectionManager.getInstance();

            PatientDAOImpl patientDAO = new PatientDAOImpl(connectionManager);
            MedecinDAOImpl medecinDAO = new MedecinDAOImpl(connectionManager);
            UserDAOImpl userDAO = new UserDAOImpl(connectionManager);

            this.patientService = new PatientServiceImpl(patientDAO);
            this.medecinService = new MedecinServiceImpl(medecinDAO);
            this.notificationService = new NotificationServiceImpl();
            this.rendezVousService = new RendezVousServiceImpl(new RendezVousDAOImpl(connectionManager, patientDAO, medecinDAO), notificationService);
            this.authenticationService = new AuthenticationServiceImpl(userDAO);
        } catch (SQLException e) {
            System.err.println("Error initializing services: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database connection or services.", e);
        }
    }

    @Override
    public Object call(Class<?> type) {
        if (type == LoginController.class) {
            return new LoginController(authenticationService);
        } else if (type == MainController.class) {
            return new MainController();
        } else if (type == PatientController.class) {
            return new PatientController(patientService);
        } else if (type == MedecinController.class) {
            return new MedecinController(medecinService);
        } else if (type == RendezVousController.class) {
            return new RendezVousController(rendezVousService, patientService, medecinService);
        }
        
        throw new IllegalArgumentException("Unknown controller class: " + type.getName());
    }
}


