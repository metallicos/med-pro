package com.medicalappointment.models;

import java.time.LocalDate;

public class Patient {
    private int id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String adresse;
    private String telephone;

    public Patient(int id, String nom, String prenom, LocalDate dateNaissance, String adresse, String telephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.adresse = adresse;
        this.telephone = telephone;
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

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }    
    @Override
    public String toString() {
        return "Patient{" +
               "id=" + id +
               ", nom=\'" + nom + '\'' +
               ", prenom=\'" + prenom + '\'' +
               ", dateNaissance=" + dateNaissance +
               ", adresse=\'" + adresse + '\'' +
               ", telephone=\'" + telephone + '\'' +
               '}';
    }
}


