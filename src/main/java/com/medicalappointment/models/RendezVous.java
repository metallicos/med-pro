
package com.medicalappointment.models;

import java.time.LocalDateTime;

public class RendezVous {
    private int id;
    private Patient patient;
    private Medecin medecin;
    private LocalDateTime dateHeure;
    private String motif;

    public RendezVous(int id, Patient patient, Medecin medecin, LocalDateTime dateHeure, String motif) {
        this.id = id;
        this.patient = patient;
        this.medecin = medecin;
        this.dateHeure = dateHeure;
        this.motif = motif;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }    
    @Override
    public String toString() {
        return patient.getNom() + " " + patient.getPrenom() + " - Dr. " + medecin.getNom() + " " + medecin.getPrenom() + 
               " (" + dateHeure.toLocalDate() + " à " + dateHeure.toLocalTime() + ")";
    }
}


