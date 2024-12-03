package com.yaeby.spring_rabbitmq_test.email;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSenderService.class);
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendSimpleEmail(String toEmail,
                                String subject,
                                String body
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        LOGGER.info("Email sent successfully to {}", toEmail);
    }

}
