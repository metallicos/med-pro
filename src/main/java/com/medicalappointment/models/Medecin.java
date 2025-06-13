
package com.medicalappointment.models;

public class Medecin {
    private int id;
    private String nom;
    private String prenom;
    private String specialite;
    private String numeroLicence;

    public Medecin(int id, String nom, String prenom, String specialite, String numeroLicence) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.specialite = specialite;
        this.numeroLicence = numeroLicence;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }    
    
    public String getNumeroLicence() {
        return numeroLicence;
    }

    public void setNumeroLicence(String numeroLicence) {
        this.numeroLicence = numeroLicence;
    }

    @Override
    public String toString() {
        return "Medecin{" +
               "id=" + id +
               ", nom=\'" + nom + '\'' +
               ", prenom=\'" + prenom + '\'' +
               ", specialite=\'" + specialite + '\'' +
               ", numeroLicence=\'" + numeroLicence + '\'' +
               '}';
    }
}


