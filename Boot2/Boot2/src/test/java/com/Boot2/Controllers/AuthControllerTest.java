package com.Boot2.Controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    // Тест метода loginPage()
    @Test
    public void testLoginPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("auth/login"));
    }

    // Тест метода registrationPage()
    @Test
    public void testRegistrationPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/registration"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("auth/registration"));
    }

    // Пример теста метода performRegistration()
    @Test
    public void testPerformRegistration() throws Exception {
        // Создание тестового JSON-объекта
        String jsonBody = "{ \"username\": \"testuser\", \"password\": \"testpassword\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.jwt-token").exists());
    }

    // Пример теста метода performLogin()
    @Test
    public void testPerformLogin() throws Exception {
        // Создание тестового JSON-объекта
        String jsonBody = "{ \"username\": \"testuser\", \"password\": \"testpassword\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.jwt-token").exists());
    }

    // Тест метода activate()
    @Test
    public void testActivate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/activate/testcode"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("message"))
                .andExpect(MockMvcResultMatchers.view().name("login"));
    }
}