package com.cncstock.controller;

import com.cncstock.service.EmailService;
import com.cncstock.service.StockNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EmailController {


    private final EmailService emailService;

    private final StockNotificationService lowStockNotificationService;

    @Autowired
    public EmailController(EmailService emailService, StockNotificationService lowStockNotificationService) {
        this.emailService = emailService;
        this.lowStockNotificationService = lowStockNotificationService;
    }

    @PostMapping("/sendEmail")
    public String sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String text) {
        emailService.sendEmailWithSignature(to, subject, text);
        return "Email sent successfully!";
    }

    @GetMapping("/take")
    public void stocktake() {
        lowStockNotificationService.stockTakeAndEmail();
    }

    @GetMapping("/crhollands")
    public void constockCHR() {
        lowStockNotificationService.emailCRHConstockLevel();
    }
}
