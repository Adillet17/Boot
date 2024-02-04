package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {
    // Автоподключение JavaMailSender
    @Autowired
    private JavaMailSender mailSender;

    // Имя пользователя для отправки писем
    @Value("${spring.mail.username}")
    private String username;

    // Метод для отправки письма
    public void send(String emailTo, String subject, String message) {
        // Создание объекта SimpleMailMessage
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        // Установка отправителя
        mailMessage.setFrom(username);

        // Установка получателя
        mailMessage.setTo(emailTo);

        // Установка темы письма
        mailMessage.setSubject(subject);

        // Установка текста письма
        mailMessage.setText(message);

        // Отправка письма с использованием JavaMailSender
        mailSender.send(mailMessage);
    }
}
