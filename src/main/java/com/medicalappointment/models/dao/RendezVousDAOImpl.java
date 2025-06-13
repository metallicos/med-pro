package com.medicalappointment.models.dao;

import com.medicalappointment.models.dao.exception.DAOException;
import com.medicalappointment.models.Medecin;
import com.medicalappointment.models.Patient;
import com.medicalappointment.models.RendezVous;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RendezVousDAOImpl implements IRendezVousDAO {

    private IDatabaseConnectionManager connectionManager;
    private IPatientDAO patientDAO;
    private IMedecinDAO medecinDAO;

    public RendezVousDAOImpl(IDatabaseConnectionManager connectionManager, IPatientDAO patientDAO, IMedecinDAO medecinDAO) {
        this.connectionManager = connectionManager;
        this.patientDAO = patientDAO;
        this.medecinDAO = medecinDAO;
    }

    @Override
    public void addRendezVous(RendezVous rendezVous) {
        String sql = "INSERT INTO rendez_vous (patient_id, medecin_id, date_heure, motif) VALUES (?, ?, ?, ?)";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, rendezVous.getPatient().getId());
            stmt.setInt(2, rendezVous.getMedecin().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(rendezVous.getDateHeure()));
            stmt.setString(4, rendezVous.getMotif());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    rendezVous.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error adding rendez-vous: " + e.getMessage(), e);
        }
    }

    @Override
    public RendezVous getRendezVousById(int id) {
        String sql = "SELECT * FROM rendez_vous WHERE id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int patientId = rs.getInt("patient_id");
                    int medecinId = rs.getInt("medecin_id");
                    Patient patient = patientDAO.getPatientById(patientId);
                    Medecin medecin = medecinDAO.getMedecinById(medecinId);

                    return new RendezVous(
                            rs.getInt("id"),
                            patient,
                            medecin,
                            rs.getTimestamp("date_heure").toLocalDateTime(),
                            rs.getString("motif")
                    );
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting rendez-vous by ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<RendezVous> getAllRendezVous() {
        List<RendezVous> rendezVousList = new ArrayList<>();
        String sql = "SELECT * FROM rendez_vous";
        try (Connection conn = connectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int patientId = rs.getInt("patient_id");
                int medecinId = rs.getInt("medecin_id");
                Patient patient = patientDAO.getPatientById(patientId);
                Medecin medecin = medecinDAO.getMedecinById(medecinId);

                rendezVousList.add(new RendezVous(
                        rs.getInt("id"),
                        patient,
                        medecin,
                        rs.getTimestamp("date_heure").toLocalDateTime(),
                        rs.getString("motif")
                ));
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting all rendez-vous: " + e.getMessage(), e);
        }
        return rendezVousList;
    }

    @Override
    public List<RendezVous> getRendezVousByDate(LocalDate date) {
        List<RendezVous> rendezVousList = new ArrayList<>();
        String sql = "SELECT * FROM rendez_vous WHERE DATE(date_heure) = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int patientId = rs.getInt("patient_id");
                    int medecinId = rs.getInt("medecin_id");
                    Patient patient = patientDAO.getPatientById(patientId);
                    Medecin medecin = medecinDAO.getMedecinById(medecinId);

                    rendezVousList.add(new RendezVous(
                            rs.getInt("id"),
                            patient,
                            medecin,
                            rs.getTimestamp("date_heure").toLocalDateTime(),
                            rs.getString("motif")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting rendez-vous by date: " + e.getMessage(), e);
        }
        return rendezVousList;
    }

    @Override
    public void updateRendezVous(RendezVous rendezVous) {
        String sql = "UPDATE rendez_vous SET patient_id = ?, medecin_id = ?, date_heure = ?, motif = ? WHERE id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rendezVous.getPatient().getId());
            stmt.setInt(2, rendezVous.getMedecin().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(rendezVous.getDateHeure()));
            stmt.setString(4, rendezVous.getMotif());
            stmt.setInt(5, rendezVous.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error updating rendez-vous: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteRendezVous(int id) {
        String sql = "DELETE FROM rendez_vous WHERE id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error deleting rendez-vous: " + e.getMessage(), e);
        }
    }
}


