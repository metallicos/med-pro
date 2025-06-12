package com.medicalappointment.dao;

import com.medicalappointment.dao.exception.DAOException;
import com.medicalappointment.model.Medecin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedecinDAOImpl implements IMedecinDAO {

    private IDatabaseConnectionManager connectionManager;

    public MedecinDAOImpl(IDatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void addMedecin(Medecin medecin) {
        String sql = "INSERT INTO medecins (nom, prenom, specialite, numero_licence) VALUES (?, ?, ?, ?)";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, medecin.getNom());
            stmt.setString(2, medecin.getPrenom());
            stmt.setString(3, medecin.getSpecialite());
            stmt.setString(4, medecin.getNumeroLicence());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    medecin.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error adding medecin: " + e.getMessage(), e);
        }
    }

    @Override
    public Medecin getMedecinById(int id) {
        String sql = "SELECT * FROM medecins WHERE id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Medecin(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("specialite"),
                            rs.getString("numero_licence")
                    );
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting medecin by ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Medecin> getAllMedecins() {
        List<Medecin> medecins = new ArrayList<>();
        String sql = "SELECT * FROM medecins";
        try (Connection conn = connectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                medecins.add(new Medecin(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("specialite"),
                        rs.getString("numero_licence")
                ));
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting all medecins: " + e.getMessage(), e);
        }
        return medecins;
    }

    @Override
    public void updateMedecin(Medecin medecin) {
        String sql = "UPDATE medecins SET nom = ?, prenom = ?, specialite = ?, numero_licence = ? WHERE id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, medecin.getNom());
            stmt.setString(2, medecin.getPrenom());
            stmt.setString(3, medecin.getSpecialite());
            stmt.setString(4, medecin.getNumeroLicence());
            stmt.setInt(5, medecin.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error updating medecin: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteMedecin(int id) {
        String sql = "DELETE FROM medecins WHERE id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error deleting medecin: " + e.getMessage(), e);
        }
    }
}


