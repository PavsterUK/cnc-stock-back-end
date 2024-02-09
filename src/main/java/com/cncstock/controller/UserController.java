package com.cncstock.controller;

import com.cncstock.model.entity.User;
import com.cncstock.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint to create a new user
    @PostMapping("/add")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint to get user details by ID
    @GetMapping("/{userId}")
    public User getUserByRotavalID(@PathVariable int userId) {
        return userService.getUserByRotavalId(userId);
    }

    // Endpoint to update user details
    @PutMapping("/{userId}")
    public User updateUser(@PathVariable int userId, @RequestBody User updatedUser) {
        return userService.updateUser(userId, updatedUser);
    }

    // Endpoint to update user password by RotavalID with admin code
    @PutMapping("/updatePassword/{rotavalId}")
    public ResponseEntity<?> updatePassword(
            @PathVariable int rotavalId,
            @RequestParam int adminCode,
            @RequestBody String newPassword
    ) {
        try {
            User updatedUser = userService.updatePasswordByRotavalId(rotavalId, adminCode, newPassword);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint to delete a user
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
    }
}
