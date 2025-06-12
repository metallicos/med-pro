package com.medicalappointment.dao;

import com.medicalappointment.dao.exception.DAOException;
import com.medicalappointment.model.Patient;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientDAOImpl implements IPatientDAO {

    private IDatabaseConnectionManager connectionManager;

    public PatientDAOImpl(IDatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void addPatient(Patient patient) {
        String sql = "INSERT INTO patients (nom, prenom, date_naissance, adresse, telephone) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, patient.getNom());
            stmt.setString(2, patient.getPrenom());
            stmt.setDate(3, Date.valueOf(patient.getDateNaissance()));
            stmt.setString(4, patient.getAdresse());
            stmt.setString(5, patient.getTelephone());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    patient.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error adding patient: " + e.getMessage(), e);
        }
    }

    @Override
    public Patient getPatientById(int id) {
        String sql = "SELECT * FROM patients WHERE id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Patient(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getDate("date_naissance").toLocalDate(),
                            rs.getString("adresse"),
                            rs.getString("telephone")
                    );
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting patient by ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients";
        try (Connection conn = connectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                patients.add(new Patient(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getDate("date_naissance").toLocalDate(),
                        rs.getString("adresse"),
                        rs.getString("telephone")
                ));
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting all patients: " + e.getMessage(), e);
        }
        return patients;
    }

    @Override
    public void updatePatient(Patient patient) {
        String sql = "UPDATE patients SET nom = ?, prenom = ?, date_naissance = ?, adresse = ?, telephone = ? WHERE id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patient.getNom());
            stmt.setString(2, patient.getPrenom());
            stmt.setDate(3, Date.valueOf(patient.getDateNaissance()));
            stmt.setString(4, patient.getAdresse());
            stmt.setString(5, patient.getTelephone());
            stmt.setInt(6, patient.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error updating patient: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletePatient(int id) {
        String sql = "DELETE FROM patients WHERE id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error deleting patient: " + e.getMessage(), e);
        }
    }
}


