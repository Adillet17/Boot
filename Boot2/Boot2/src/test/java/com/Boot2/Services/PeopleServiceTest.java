package com.Boot2.Services;

import com.models.Person;
import com.repositories.PeopleRepository;
import com.services.PeopleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
@AutoConfigureMockMvc
public class PeopleServiceTest {
    @MockBean
    PeopleRepository peopleRepository;

    @Autowired
    PeopleService peopleService;

    Person testPerson;

    List<Person> testPeople;


    @BeforeEach
    void setUp() {
        testPerson = new Person();
        testPerson.setUsername("Test");

        testPeople = new ArrayList<>();
        testPeople.add(testPerson);
    }

    @Test
    void findAllTest() {
        // given
        when(peopleRepository.findAll()).thenReturn(testPeople);
        // when
        List<Person> receivedPeople = peopleService.findAll();
        // then
        assertIterableEquals(testPeople, receivedPeople);
        verify(peopleRepository).findAll();
    }

    @Test
    void findOneByIdTest() {
        // given
        when(peopleRepository.findById(anyInt())).thenReturn(Optional.of(testPerson));
        // when
        Person receivedPerson = peopleService.findOneById(anyInt());
        // then
        assertNotNull(receivedPerson);
        assertEquals("Test", receivedPerson.getUsername());
        verify(peopleRepository, times(1)).findById(anyInt());
    }
    @Test
    void saveTest() {
        // given, when
        for (int i = 0; i < 5; i++) peopleService.save(new Person());
        // then
        verify(peopleRepository, times(5)).save(any(Person.class));
    }
    @Test
    void testLoadUserByUsername() {
        // Мокирование репозитория
        when(peopleRepository.findByName(any())).thenReturn(Optional.of(new Person()));

        // Вызов метода сервиса
        UserDetails userDetails = peopleService.loadUserByUsername("test");

        // Проверка результатов
        assertNotNull(userDetails);
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        // Мокирование репозитория
        when(peopleRepository.findByName(any())).thenReturn(Optional.empty());

        // Вызов метода сервиса и проверка на исключение

        assertThrows(UsernameNotFoundException.class, () -> peopleService.loadUserByUsername("test"));
    }
}

