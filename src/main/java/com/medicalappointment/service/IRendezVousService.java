
package com.medicalappointment.service;

import com.medicalappointment.model.RendezVous;
import java.time.LocalDate;
import java.util.List;

public interface IRendezVousService {
    void addRendezVous(RendezVous rendezVous);
    RendezVous getRendezVousById(int id);
    List<RendezVous> getAllRendezVous();
    List<RendezVous> getRendezVousByDate(LocalDate date);
    void updateRendezVous(RendezVous rendezVous);
    void deleteRendezVous(int id);
}


