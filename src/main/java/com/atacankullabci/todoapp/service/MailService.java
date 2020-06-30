package com.atacankullabci.todoapp.service;

import com.atacankullabci.todoapp.common.NotificationMail;
import com.atacankullabci.todoapp.exceptions.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender javaMailSender;

    public MailService(@Qualifier("configMailSender") JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendActivationMail(NotificationMail notificationMail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(((JavaMailSenderImpl) javaMailSender).getUsername());
        message.setTo(notificationMail.getRecipient());
        message.setSubject(notificationMail.getSubject());
        message.setText(notificationMail.getBody());

        // Go to the sender gmail account -> security tab -> enable less secure app access
        try {
            javaMailSender.send(message);
            log.info("Activation mail send to user {}", notificationMail.getRecipient());
        } catch (MailException err) {
            new CustomException("An error occurred while sending the email.");
        }
    }
}
