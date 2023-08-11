package com.cncstock.service;

import com.cncstock.model.User;
import com.cncstock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        Optional<User> existingUser = userRepository.findById(user.getRotavalID());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User with the same rotaval ID already exists.");
        }
        return userRepository.save(user);
    }

    public User getUserByRotavalId(int userId) {
        // Perform business logic related to retrieving a user by ID (e.g., handling exceptions)
        return userRepository.findById(userId).orElse(null);
    }

    public User updateUser(int userId, User updatedUser) {
        // Perform business logic related to updating user details (e.g., validation, updating fields)
        User existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser != null) {
            // Update the user details with the provided data
            existingUser.setName(updatedUser.getName());
            existingUser.setSurname(updatedUser.getSurname());
            existingUser.setPassword(updatedUser.getPassword());
            return userRepository.save(existingUser);
        }
        return null;
    }

    public User updatePasswordByRotavalId(int rotavalId, int adminCode, String newPassword) {

        // Check if the user with the provided rotavalId and adminCode exists
        User existingUser = userRepository.findById(rotavalId).orElse(null);
        if (existingUser == null) {
            throw new IllegalArgumentException("User with the provided rotaval ID does not exist.");
        }

        int validAdminCode = 12345; // Admin code hardcoded for the time being.
        if (adminCode != validAdminCode) {
            throw new IllegalArgumentException("Invalid admin code.");
        }
        // Update the user's password
        existingUser.setPassword(newPassword);
        return userRepository.save(existingUser);
    }

    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }
}
