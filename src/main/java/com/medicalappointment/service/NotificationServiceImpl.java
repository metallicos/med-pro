
package com.medicalappointment.service;

import com.medicalappointment.models.RendezVous;

public class NotificationServiceImpl implements INotificationService {

    @Override
    public void sendReminder(RendezVous rendezVous) {
        System.out.println("Sending reminder for appointment: " + rendezVous.getMotif() + 
                           " with " + rendezVous.getMedecin().getPrenom() + " " + rendezVous.getMedecin().getNom() +
                           " on " + rendezVous.getDateHeure());
        // In a real application, this would involve sending emails, SMS, or in-app notifications.
    }
}


