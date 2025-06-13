package com.medicalappointment.models.dao;

import com.medicalappointment.models.RendezVous;
import java.util.List;
import java.time.LocalDate;

public interface IRendezVousDAO {
    void addRendezVous(RendezVous rendezVous);
    RendezVous getRendezVousById(int id);
    List<RendezVous> getAllRendezVous();
    List<RendezVous> getRendezVousByDate(LocalDate date);
    void updateRendezVous(RendezVous rendezVous);
    void deleteRendezVous(int id);
}


