package com.medicalappointment.models.dao;

import com.medicalappointment.models.Patient;
import java.util.List;

public interface IPatientDAO {
    void addPatient(Patient patient);
    Patient getPatientById(int id);
    List<Patient> getAllPatients();
    void updatePatient(Patient patient);
    void deletePatient(int id);
}


