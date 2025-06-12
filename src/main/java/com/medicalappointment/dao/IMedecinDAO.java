package com.medicalappointment.dao;

import com.medicalappointment.model.Medecin;
import java.util.List;

public interface IMedecinDAO {
    void addMedecin(Medecin medecin);
    Medecin getMedecinById(int id);
    List<Medecin> getAllMedecins();
    void updateMedecin(Medecin medecin);
    void deleteMedecin(int id);
}


