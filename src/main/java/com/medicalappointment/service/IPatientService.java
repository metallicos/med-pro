package com.medicalappointment.service;

import com.medicalappointment.model.Patient;
import java.util.List;

public interface IPatientService {
    void addPatient(Patient patient);
    Patient getPatientById(int id);
    List<Patient> getAllPatients();
    void updatePatient(Patient patient);
    void deletePatient(int id);
}


