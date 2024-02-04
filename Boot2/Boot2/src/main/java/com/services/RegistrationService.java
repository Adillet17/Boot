package com.services;

import com.models.Person;
import com.repositories.PeopleRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.UUID;

@Service
public class RegistrationService {

    // Поддерживает кодирование паролей
    private final PasswordEncoder passwordEncoder;

    // Репозиторий для работы с данными о людях
    private final PeopleRepository peopleRepository;

    // Сервис для отправки электронной почты
    private final MailSenderService mailSenderService;

    // Конструктор, инъекция зависимостей
    public RegistrationService(PeopleRepository peopleRepository,
                               PasswordEncoder passwordEncoder, MailSenderService mailSenderService) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSenderService = mailSenderService;
    }

    // Метод регистрации пользователя
    @Transactional
    public void register(@Valid Person person) {
        // Кодируем пароль
        String encodedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);

        // Генерируем уникальный код активации
        person.setActivationCode(UUID.randomUUID().toString());

        // Если у пользователя указан email, отправляем письмо с кодом активации
        if (!StringUtils.isEmpty(person.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Sweater. Please, visit the following link to activate your account: http://localhost:8080/activate/%s",
                    person.getUsername(),
                    person.getActivationCode()
            );
            mailSenderService.send(person.getEmail(), "Activation code", message);
        }

        // Сохраняем информацию о пользователе
        peopleRepository.save(person);
    }

    // Метод активации пользователя по коду
    public boolean activateUser(String code) {
        // Поиск пользователя по коду активации
        Person person = peopleRepository.findByActivationCode(code);

        // Если пользователь не найден, возвращаем false
        if (person == null)
            return false;

        // Сбрасываем код активации и сохраняем пользователя
        person.setActivationCode(null);
        peopleRepository.save(person);

        // Возвращаем true, пользователь активирован
        return true;
    }
}