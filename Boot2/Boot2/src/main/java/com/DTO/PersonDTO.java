package com.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PersonDTO {

    // Аннотация @NotNull гарантирует, что поле не может быть пустым
    // Аннотация @Size определяет минимальное и максимальное количество символов для поля
    @NotNull(message = "ФИО не может быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
    private String username;

    // Аннотация @Min определяет минимальное значение для числового поля
    // Здесь применяется для года рождения, чтобы удостовериться, что он больше 1900
    @Min(value = 1900, message = "Год рождения должен быть больше 1900")
    private int year_of_birth;

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

    // Геттер для года рождения
    public int getYear_of_birth() {
        return year_of_birth;
    }

    // Сеттер для года рождения
    public void setYear_of_birth(int year_of_birth) {
        this.year_of_birth = year_of_birth;
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