
package com.medicalappointment.service;

import com.medicalappointment.models.dao.IMedecinDAO;
import com.medicalappointment.models.dao.exception.DAOException;
import com.medicalappointment.models.Medecin;
import com.medicalappointment.service.exception.ServiceException;

import java.util.List;

public class MedecinServiceImpl implements IMedecinService {

    private IMedecinDAO medecinDAO;

    public MedecinServiceImpl(IMedecinDAO medecinDAO) {
        this.medecinDAO = medecinDAO;
    }

    @Override
    public void addMedecin(Medecin medecin) {
        if (medecin == null) {
            throw new ServiceException("Medecin cannot be null.");
        }
        if (medecin.getNom() == null || medecin.getNom().trim().isEmpty()) {
            throw new ServiceException("Medecin name cannot be empty.");
        }
        if (medecin.getPrenom() == null || medecin.getPrenom().trim().isEmpty()) {
            throw new ServiceException("Medecin surname cannot be empty.");
        }
        // Add more validation rules as needed

        try {
            medecinDAO.addMedecin(medecin);
        } catch (DAOException e) {
            throw new ServiceException("Failed to add medecin: " + e.getMessage(), e);
        }
    }

    @Override
    public Medecin getMedecinById(int id) {
        if (id <= 0) {
            throw new ServiceException("Medecin ID must be positive.");
        }
        try {
            return medecinDAO.getMedecinById(id);
        } catch (DAOException e) {
            throw new ServiceException("Failed to retrieve medecin: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Medecin> getAllMedecins() {
        try {
            return medecinDAO.getAllMedecins();
        } catch (DAOException e) {
            throw new ServiceException("Failed to retrieve all medecins: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateMedecin(Medecin medecin) {
        if (medecin == null) {
            throw new ServiceException("Medecin cannot be null.");
        }
        if (medecin.getId() <= 0) {
            throw new ServiceException("Medecin ID must be positive for update.");
        }
        if (medecin.getNom() == null || medecin.getNom().trim().isEmpty()) {
            throw new ServiceException("Medecin name cannot be empty.");
        }
        if (medecin.getPrenom() == null || medecin.getPrenom().trim().isEmpty()) {
            throw new ServiceException("Medecin surname cannot be empty.");
        }
        // Add more validation rules as needed

        try {
            medecinDAO.updateMedecin(medecin);
        } catch (DAOException e) {
            throw new ServiceException("Failed to update medecin: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteMedecin(int id) {
        if (id <= 0) {
            throw new ServiceException("Medecin ID must be positive for deletion.");
        }
        try {
            medecinDAO.deleteMedecin(id);
        } catch (DAOException e) {
            throw new ServiceException("Failed to delete medecin: " + e.getMessage(), e);
        }
    }
}


