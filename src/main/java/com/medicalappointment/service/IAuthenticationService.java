package com.medicalappointment.service;

import com.medicalappointment.model.User;

public interface IAuthenticationService {
    User authenticate(String username, String password);
    void logout(User user);
    boolean isAuthorized(User user, String requiredRole);
}
