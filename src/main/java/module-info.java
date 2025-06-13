module com.medicalappointment {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;

    opens com.medicalappointment to javafx.fxml;
    opens com.medicalappointment.controllers to javafx.fxml;
    opens com.medicalappointment.models to javafx.base;

    exports com.medicalappointment;
    exports com.medicalappointment.controllers;
    exports com.medicalappointment.models;
    exports com.medicalappointment.service;
    exports com.medicalappointment.service.exception;
    exports com.medicalappointment.models.dao;
    exports com.medicalappointment.models.dao.exception;
}


