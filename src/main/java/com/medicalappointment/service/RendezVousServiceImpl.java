package com.medicalappointment.service;

import com.medicalappointment.models.dao.IRendezVousDAO;
import com.medicalappointment.models.dao.exception.DAOException;
import com.medicalappointment.models.RendezVous;
import com.medicalappointment.service.exception.ServiceException;

import java.time.LocalDate;
import java.util.List;

public class RendezVousServiceImpl implements IRendezVousService {

    private IRendezVousDAO rendezVousDAO;
    private INotificationService notificationService;

    public RendezVousServiceImpl(IRendezVousDAO rendezVousDAO, INotificationService notificationService) {
        this.rendezVousDAO = rendezVousDAO;
        this.notificationService = notificationService;
    }

    @Override
    public void addRendezVous(RendezVous rendezVous) {
        if (rendezVous == null) {
            throw new ServiceException("RendezVous cannot be null.");
        }
        if (rendezVous.getPatient() == null) {
            throw new ServiceException("RendezVous must have a patient.");
        }
        if (rendezVous.getMedecin() == null) {
            throw new ServiceException("RendezVous must have a medecin.");
        }
        if (rendezVous.getDateHeure() == null) {
            throw new ServiceException("RendezVous date and time cannot be null.");
        }
        if (rendezVous.getMotif() == null || rendezVous.getMotif().trim().isEmpty()) {
            throw new ServiceException("RendezVous motif cannot be empty.");
        }
        // Add more validation rules as needed, e.g., check for overlapping appointments

        try {
            rendezVousDAO.addRendezVous(rendezVous);
            notificationService.sendReminder(rendezVous); // Send reminder after adding
        } catch (DAOException e) {
            throw new ServiceException("Failed to add rendez-vous: " + e.getMessage(), e);
        }
    }

    @Override
    public RendezVous getRendezVousById(int id) {
        if (id <= 0) {
            throw new ServiceException("RendezVous ID must be positive.");
        }
        try {
            return rendezVousDAO.getRendezVousById(id);
        } catch (DAOException e) {
            throw new ServiceException("Failed to retrieve rendez-vous: " + e.getMessage(), e);
        }
    }

    @Override
    public List<RendezVous> getAllRendezVous() {
        try {
            return rendezVousDAO.getAllRendezVous();
        } catch (DAOException e) {
            throw new ServiceException("Failed to retrieve all rendez-vous: " + e.getMessage(), e);
        }
    }

    @Override
    public List<RendezVous> getRendezVousByDate(LocalDate date) {
        if (date == null) {
            throw new ServiceException("Date cannot be null.");
        }
        try {
            return rendezVousDAO.getRendezVousByDate(date);
        } catch (DAOException e) {
            throw new ServiceException("Failed to retrieve rendez-vous by date: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateRendezVous(RendezVous rendezVous) {
        if (rendezVous == null) {
            throw new ServiceException("RendezVous cannot be null.");
        }
        if (rendezVous.getId() <= 0) {
            throw new ServiceException("RendezVous ID must be positive for update.");
        }
        if (rendezVous.getPatient() == null) {
            throw new ServiceException("RendezVous must have a patient.");
        }
        if (rendezVous.getMedecin() == null) {
            throw new ServiceException("RendezVous must have a medecin.");
        }
        if (rendezVous.getDateHeure() == null) {
            throw new ServiceException("RendezVous date and time cannot be null.");
        }
        if (rendezVous.getMotif() == null || rendezVous.getMotif().trim().isEmpty()) {
            throw new ServiceException("RendezVous motif cannot be empty.");
        }
        // Add more validation rules as needed

        try {
            rendezVousDAO.updateRendezVous(rendezVous);
            notificationService.sendReminder(rendezVous); // Send reminder after updating
        } catch (DAOException e) {
            throw new ServiceException("Failed to update rendez-vous: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteRendezVous(int id) {
        if (id <= 0) {
            throw new ServiceException("RendezVous ID must be positive for deletion.");
        }
        try {
            rendezVousDAO.deleteRendezVous(id);
        } catch (DAOException e) {
            throw new ServiceException("Failed to delete rendez-vous: " + e.getMessage(), e);
        }
    }
}


