
package com.Boot2.Services;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest

@TestMethodOrder(MethodOrderer.Random.class)
class MailSenderServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Spy
    @InjectMocks
    private com.services.MailSenderService mailSenderService;

    @Test
    void testSend() {
        // Подготовка данных для теста
        String emailTo = "test@example.com";
        String subject = "Test Subject";
        String message = "Test Message";

        // Мокирование статического метода JavaMailSender.send
        Mockito.mockStatic(JavaMailSender.class);

        // Вызов метода send в MailSenderService
        mailSenderService.send(emailTo, subject, message);

        // Проверка вызова метода send в JavaMailSender
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}