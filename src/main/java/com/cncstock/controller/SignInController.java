package com.cncstock.controller;

import com.cncstock.model.User;
import com.cncstock.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SignInController {

    private final UserService userService;

    @Autowired
    public SignInController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin
    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestParam(required = false) Integer rotavalId, @RequestParam String password) {
        User user = userService.getUserByRotavalId(rotavalId);

        if (user == null) {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }

        // Compare the provided plain text password with the stored password
        if (password.equals(user.getPassword())) {
            // Successful sign-in
            return new ResponseEntity<>(user.getName()+ " " + user.getSurname() , HttpStatus.OK);
        } else {
            // Invalid password
            return new ResponseEntity<>("Invalid password.", HttpStatus.UNAUTHORIZED);
        }
    }
}