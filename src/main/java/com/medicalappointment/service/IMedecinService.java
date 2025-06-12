
package com.medicalappointment.service;

import com.medicalappointment.model.Medecin;
import java.util.List;

public interface IMedecinService {
    void addMedecin(Medecin medecin);
    Medecin getMedecinById(int id);
    List<Medecin> getAllMedecins();
    void updateMedecin(Medecin medecin);
    void deleteMedecin(int id);
}


