
package com.medicalappointment.service;

import com.medicalappointment.models.RendezVous;

public interface INotificationService {
    void sendReminder(RendezVous rendezVous);
}


