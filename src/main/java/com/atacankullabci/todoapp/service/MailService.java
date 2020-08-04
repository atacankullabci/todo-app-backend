package com.atacankullabci.todoapp.service;

import com.atacankullabci.todoapp.common.NotificationMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSenderImpl javaMailSenderImpl;

    public MailService(@Qualifier("configMailSender") JavaMailSenderImpl javaMailSenderImpl) {
        this.javaMailSenderImpl = javaMailSenderImpl;
    }

    @Async
    public void sendActivationMail(NotificationMail notificationMail) {
        Session session = Session.getInstance(javaMailSenderImpl.getJavaMailProperties(),
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(javaMailSenderImpl.getUsername(), javaMailSenderImpl.getPassword());
                    }
                });

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress("atacan235@gmail.com"));

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(notificationMail.getRecipient()));

            message.setSubject(notificationMail.getSubject());

            message.setContent(notificationMail.getBody(), "text/html");

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
