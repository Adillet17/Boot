package com.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AuthenticationDTO {

    // Аннотация @NotNull гарантирует, что поле не может быть пустым
    // Аннотация @Size определяет минимальное и максимальное количество символов для поля
    @NotNull(message = "ФИО не может быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
    private String username;

    // Поле для пароля
    private String password;

    // Геттер для имени пользователя
    public String getUsername() {
        return username;
    }

    // Сеттер для имени пользователя
    public void setUsername(String username) {
        this.username = username;
    }

    // Геттер для пароля
    public String getPassword() {
        return password;
    }

    // Сеттер для пароля
    public void setPassword(String password) {
        this.password = password;
    }
}
