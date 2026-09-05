package com.kgm.nextnest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationOtp(
            String email,
            String otp
    ) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(email);

        message.setSubject(
                "NextNest Email Verification"
        );

        message.setText(
                "Your verification code is: "
                        + otp
                        + "\n\nValid for 10 minutes."
        );

        mailSender.send(message);
    }

    public void sendNewMessageNotification(
            String receiverEmail,
            String senderName,
            String apartmentTitle,
            String messageContent
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(receiverEmail);

        message.setSubject("New Message About Your Apartment");

        message.setText(
                "Hello,\n\n"

                        + senderName
                        + " sent you a new message regarding:\n\n"
                        + apartmentTitle
                        + "\n\nMessage:\n"
                        + messageContent
                        + "\n\nPlease login to NextNest to reply."

        );

        mailSender.send(message);
    }
}