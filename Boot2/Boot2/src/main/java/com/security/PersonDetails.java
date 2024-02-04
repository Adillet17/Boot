package com.security;


import com.models.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class PersonDetails implements UserDetails {
    private Person person;

    // Конструктор класса
    public PersonDetails(Person person) {
        this.person = person;
    }

    // Получение ролей пользователя (в данной реализации всегда null)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    // Получение пароля пользователя
    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    // Получение имени пользователя
    @Override
    public String getUsername() {
        return this.person.getUsername();
    }

    // Методы, указывающие на состояние учетной записи пользователя
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Получение объекта Person, связанного с UserDetails
    public Person getPerson() {
        return this.person;
    }
}
