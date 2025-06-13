package com.medicalappointment.service;

import com.medicalappointment.models.User;

public interface IAuthenticationService {
    User authenticate(String username, String password);
    void logout(User user);
    boolean isAuthorized(User user, String requiredRole);
}
