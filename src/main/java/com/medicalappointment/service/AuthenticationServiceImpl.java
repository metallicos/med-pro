package com.medicalappointment.service;

import com.medicalappointment.models.dao.IUserDAO;
import com.medicalappointment.models.User;
import com.medicalappointment.service.exception.ServiceException;

public class AuthenticationServiceImpl implements IAuthenticationService {
    private final IUserDAO userDAO;

    public AuthenticationServiceImpl(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new ServiceException("Username cannot be empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ServiceException("Password cannot be empty.");
        }

        User user = userDAO.getUserByUsername(username);
        if (user == null || !user.isActive()) {
            throw new ServiceException("Invalid username or password.");
        }

        // In a real application, you should use proper password hashing
        // For example: BCrypt.checkpw(password, user.getPasswordHash())
        if (!user.getPasswordHash().equals(password)) {
            throw new ServiceException("Invalid username or password.");
        }

        userDAO.updateLastLogin(user.getId());
        return user;
    }

    @Override
    public void logout(User user) {
        // You can add any cleanup or logging logic here
    }

    @Override
    public boolean isAuthorized(User user, String requiredRole) {
        if (user == null || !user.isActive()) {
            return false;
        }

        // Admin has access to everything
        if (user.getRole().equals("ADMIN")) {
            return true;
        }

        // Check if user's role matches required role
        return user.getRole().equals(requiredRole);
    }
}
