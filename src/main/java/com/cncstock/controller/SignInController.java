package com.cncstock.controller;

import com.cncstock.model.User;
import com.cncstock.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")

public class SignInController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SignInController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestParam int rotavalId, @RequestParam String password) {

        System.out.println(rotavalId );
        System.out.println(password );

        User user = userService.getUserByRotavalId(rotavalId);

        if (user == null) {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }

        if (passwordEncoder.matches(password, user.getPassword())) {
            // Successful sign-in
            return new ResponseEntity<>("Sign-in successful.", HttpStatus.OK);
        } else {
            // Invalid password
            return new ResponseEntity<>("Invalid password.", HttpStatus.UNAUTHORIZED);
        }
    }
}