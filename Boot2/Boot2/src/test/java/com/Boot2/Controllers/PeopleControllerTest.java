package com.Boot2.Controllers;

import com.controllers.PeopleController;
import com.models.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.Random.class)
public class PeopleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private com.services.PeopleService peopleService;

    @InjectMocks
    private com.controllers.PeopleController peopleController;

    private com.models.Person testPerson;
    private List<com.models.Person> testPeople;

    @BeforeEach
    void setUp() {
        testPerson = new Person();
        testPerson.setId(generateRandomId());
        testPeople = new ArrayList<>();


        mockMvc = MockMvcBuilders.standaloneSetup(peopleController).build();
    }

    private int generateRandomId() {
        return new Random().nextInt(200);
    }

    @Test
    void indexTest() throws Exception {
        when(peopleService.findAll()).thenReturn(testPeople);
        mockMvc.perform(get("/people/index"))
                .andExpectAll(
                        model().size(1),
                        model().attribute("people", testPeople),
                        status().isOk(),
                        forwardedUrl("people/index")
                );
        verify(peopleService, times(1)).findAll();
    }



    @Test
    void getPersonTest() throws Exception {
        when(peopleService.findOneById(anyInt())).thenReturn(testPerson);

        //test with empty list of books
        mockMvc.perform(get("/people/{id}", anyInt()))
                .andExpectAll(
                        model().size(1),
                        model().attribute("person", testPerson),
                        status().isOk(),
                        forwardedUrl("people/profile")
                );
        verify(peopleService, times(1)).findOneById(anyInt());

    }

    @Test
    void newPersonTest() throws Exception {
        mockMvc.perform(get("/people/new"))
                .andExpectAll(
                        model().size(1),
                        model().attributeExists("person"),
                        status().isOk(),
                        forwardedUrl("people/new")
                );
    }

    @Test
    void createPersonTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new PeopleController(
                peopleService)).build();

        // test an empty new person
        mockMvc.perform(post("/people")
                        .flashAttr("person", testPerson))
                .andExpectAll(
                        model().size(1),
                        model().attribute("person", testPerson),
                        model().attributeErrorCount("person", 2),
                        status().isOk(),
                        forwardedUrl("people/new")
                );

        // test a person with not valid fields
        testPerson.setUsername("Name Surname Patronymic");
        testPerson.setYear_of_birth(1550);
        mockMvc.perform(post("/people")
                        .flashAttr("person", testPerson))
                .andExpectAll(
                        model().size(1),
                        model().attribute("person", testPerson),
                        model().attributeErrorCount("person", 2),
                        status().isOk(),
                        forwardedUrl("people/new")
                );

        // test a person with year earlier than 1900
        testPerson.setUsername("Фамилия Имя Отчество");
        mockMvc.perform(post("/people")
                        .flashAttr("person", testPerson))
                .andExpectAll(
                        model().size(1),
                        model().attribute("person", testPerson),
                        model().attributeErrorCount("person", 1),
                        status().isOk(),
                        forwardedUrl("people/new")
                );

        // test a person with valid fields
        testPerson.setYear_of_birth(2023);
        mockMvc.perform(post("/people")
                        .flashAttr("person", testPerson))
                .andExpectAll(
                        model().size(1),
                        model().attribute("person", testPerson),
                        model().attributeHasNoErrors("person"),
                        status().is3xxRedirection(),
                        redirectedUrl("/library/people")
                );
        verify(peopleService, times(1)).save(any(Person.class));
    }



}
