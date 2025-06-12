package com.medicalappointment.dao;

import com.medicalappointment.model.Patient;
import java.util.List;

public interface IPatientDAO {
    void addPatient(Patient patient);
    Patient getPatientById(int id);
    List<Patient> getAllPatients();
    void updatePatient(Patient patient);
    void deletePatient(int id);
}


