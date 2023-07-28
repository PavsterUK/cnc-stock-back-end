package com.cncstock.service;

import com.cncstock.model.User;
import com.cncstock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        // Perform business logic related to creating a user (e.g., validation, password hashing)
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

    public void deleteUser(int userId) {
        // Perform business logic related to deleting a user (e.g., additional checks, cascading delete)
        userRepository.deleteById(userId);
    }
}
