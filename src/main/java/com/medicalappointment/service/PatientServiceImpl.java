package com.medicalappointment.service;

import com.medicalappointment.models.dao.IPatientDAO;
import com.medicalappointment.models.dao.exception.DAOException;
import com.medicalappointment.models.Patient;
import com.medicalappointment.service.exception.ServiceException;

import java.util.List;

public class PatientServiceImpl implements IPatientService {

    private IPatientDAO patientDAO;

    public PatientServiceImpl(IPatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

    @Override
    public void addPatient(Patient patient) {
        if (patient == null) {
            throw new ServiceException("Patient cannot be null.");
        }
        if (patient.getNom() == null || patient.getNom().trim().isEmpty()) {
            throw new ServiceException("Patient name cannot be empty.");
        }
        if (patient.getPrenom() == null || patient.getPrenom().trim().isEmpty()) {
            throw new ServiceException("Patient surname cannot be empty.");
        }
        // Add more validation rules as needed

        try {
            patientDAO.addPatient(patient);
        } catch (DAOException e) {
            throw new ServiceException("Failed to add patient: " + e.getMessage(), e);
        }
    }

    @Override
    public Patient getPatientById(int id) {
        if (id <= 0) {
            throw new ServiceException("Patient ID must be positive.");
        }
        try {
            return patientDAO.getPatientById(id);
        } catch (DAOException e) {
            throw new ServiceException("Failed to retrieve patient: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Patient> getAllPatients() {
        try {
            return patientDAO.getAllPatients();
        } catch (DAOException e) {
            throw new ServiceException("Failed to retrieve all patients: " + e.getMessage(), e);
        }
    }

    @Override
    public void updatePatient(Patient patient) {
        if (patient == null) {
            throw new ServiceException("Patient cannot be null.");
        }
        if (patient.getId() <= 0) {
            throw new ServiceException("Patient ID must be positive for update.");
        }
        if (patient.getNom() == null || patient.getNom().trim().isEmpty()) {
            throw new ServiceException("Patient name cannot be empty.");
        }
        if (patient.getPrenom() == null || patient.getPrenom().trim().isEmpty()) {
            throw new ServiceException("Patient surname cannot be empty.");
        }
        // Add more validation rules as needed

        try {
            patientDAO.updatePatient(patient);
        } catch (DAOException e) {
            throw new ServiceException("Failed to update patient: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletePatient(int id) {
        if (id <= 0) {
            throw new ServiceException("Patient ID must be positive for deletion.");
        }
        try {
            patientDAO.deletePatient(id);
        } catch (DAOException e) {
            throw new ServiceException("Failed to delete patient: " + e.getMessage(), e);
        }
    }
}


