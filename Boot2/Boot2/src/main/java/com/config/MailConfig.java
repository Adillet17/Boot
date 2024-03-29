package com.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Value("${mail.debug}")
    private String debug;

    @Bean
    public JavaMailSender getMailSender(){
        // Создание экземпляра JavaMailSenderImpl
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Установка хоста, порта, имени пользователя и пароля для отправки почты
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

// Получение свойств JavaMail из созданного JavaMailSenderImpl
        Properties properties = mailSender.getJavaMailProperties();

        // Установка протокола и режима отладки из application.properties
        properties.setProperty("mail.transport.protocol", protocol);
        properties.setProperty("mail.debug", debug);

// Возврат созданного JavaMailSenderImpl в качестве бина
        return mailSender;
    }
}
