package com.Boot2.Services;

import com.models.Person;
import com.repositories.PeopleRepository;
import com.services.MailSenderService;
import com.services.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationServiceTest {

    @Mock
    private PeopleRepository peopleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailSenderService mailSenderService;

    @InjectMocks
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        // Создание тестовых данных
        Person person = new Person();
        person.setUsername("testUser");
        person.setPassword("testPassword");
        person.setEmail("test@example.com");

        // Мокирование методов
        when(passwordEncoder.encode("testPassword")).thenReturn("encodedPassword");
        when(peopleRepository.save(any())).thenReturn(person);

        // Вызов метода сервиса
        registrationService.register(person);

        // Проверка результатов
        assertNotNull(person.getActivationCode());
        assertEquals("encodedPassword", person.getPassword());

        // Проверка вызовов методов
        verify(peopleRepository, times(1)).save(person);
        verify(mailSenderService, times(1)).send(eq("test@example.com"), anyString(), anyString());
    }

    @Test
    void testActivateUser() {
        // Создание тестовых данных
        String activationCode = UUID.randomUUID().toString();
        Person person = new Person();
        person.setActivationCode(activationCode);

        // Мокирование метода
        when(peopleRepository.findByActivationCode(activationCode)).thenReturn(person);

        // Вызов метода сервиса
        boolean result = registrationService.activateUser(activationCode);

        // Проверка результатов
        assertTrue(result);
        assertNull(person.getActivationCode());

        // Проверка вызова метода
        verify(peopleRepository, times(1)).save(person);
    }

    @Test
    void testActivateUserNotFound() {
        // Мокирование метода
        when(peopleRepository.findByActivationCode(anyString())).thenReturn(null);

        // Вызов метода сервиса
        boolean result = registrationService.activateUser("nonexistentCode");

        // Проверка результатов
        assertFalse(result);

        // Проверка вызова метода
        verify(peopleRepository, never()).save(any());
    }
}