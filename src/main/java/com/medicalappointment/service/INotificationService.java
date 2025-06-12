
package com.medicalappointment.service;

import com.medicalappointment.model.RendezVous;

public interface INotificationService {
    void sendReminder(RendezVous rendezVous);
}


