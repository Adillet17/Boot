
package com.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;


@Entity
@Table(name = "person")
public class Person {

    // Идентификатор пользователя
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    // Имя пользователя
    @NotNull(message = "ФИО не может быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
    @Column(name = "name")
    private String username;

    // Год рождения пользователя
    @Min(value = 1900, message = "Год рождения должен быть больше 1900")
    @Column(name = "year_of_birth")
    private int year_of_birth;

    // Пароль пользователя
    @Column(name = "password")
    private String password;

    // Email пользователя
    @NotEmpty(message = "Email должен быть указан")
    @Email(message = "Email должен быть корректным")
    @Column(name = "email")
    private String email;

    // Код активации (для активации учетной записи)
    @Column(name = "activationCode")
    public String activationCode;

    // Конструкторы

    public Person() {
    }

    public Person(int id, String username, int year_of_birth, String password, String email, String activationCode) {
        this.id = id;
        this.username = username;
        this.year_of_birth = year_of_birth;
        this.password = password;
        this.email = email;
        this.activationCode = activationCode;
    }

    // Геттеры и сеттеры для полей

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getYear_of_birth() {
        return year_of_birth;
    }

    public void setYear_of_birth(int year_of_birth) {
        this.year_of_birth = year_of_birth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }


    // Метод toString для удобства отладки и вывода информации

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", year_of_birth=" + year_of_birth +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", activationCode='" + activationCode + '\'' +
                '}';
    }
}