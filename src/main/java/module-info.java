module com.medicalappointment {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;

    opens com.medicalappointment to javafx.fxml;
    opens com.medicalappointment.controller to javafx.fxml;
    opens com.medicalappointment.model to javafx.base;

    exports com.medicalappointment;
    exports com.medicalappointment.controller;
    exports com.medicalappointment.model;
    exports com.medicalappointment.service;
    exports com.medicalappointment.service.exception;
    exports com.medicalappointment.dao;
    exports com.medicalappointment.dao.exception;
}


