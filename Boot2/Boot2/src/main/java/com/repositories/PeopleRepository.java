
package com.repositories;


import java.util.List;

import com.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {

    // Метод для поиска пользователя по имени
    Optional<Person> findByName(String name);

    // Метод для поиска пользователя по коду активации
    Person findByActivationCode(String code);
}