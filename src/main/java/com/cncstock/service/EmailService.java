package com.cncstock.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.sql.Date;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmailWithSignature(String to, String subject, String text) {
        String emailSignature = "<br><br><br>Wishing you terabytes of joy, <br>Your Friendly Stock Assistant";
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(new InternetAddress("stockrobotwally@gmail.com", "WALL-E Stock Assistant"));

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text + "<br><br>" + emailSignature, true);

            emailSender.send(message);
        } catch (MessagingException e) {

            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}

