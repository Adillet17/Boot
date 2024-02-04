package com.services;

import com.models.Person;
import com.repositories.PeopleRepository;
import com.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService implements UserDetailsService {
    // Репозиторий для работы с данными о людях
    private final PeopleRepository peopleRepository;

    // Конструктор, инъекция зависимости репозитория
    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    // Метод для поиска всех людей
    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    // Метод для поиска человека по ID
    public Person findOneById(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        return person.orElse(null);
    }

    // Метод для сохранения информации о человеке
    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    // Метод, необходимый для реализации интерфейса UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // Поиск человека по имени
        Optional<Person> person = peopleRepository.findByName(s);

        // Если человек не найден, выбрасываем исключение
        if (person.isEmpty())
            throw new UsernameNotFoundException("User not found!");

        // Возвращаем объект PersonDetails, реализующий интерфейс UserDetails
        return new PersonDetails(person.get());
    }
}


















//    public List<Person> findAll() {
//        return peopleRepository.findAll();
//    }
//
//    public Person findOneById(int id) {
//        Optional<Person> person = peopleRepository.findById(id);
//        return person.orElse(null);
//    }
//
//    public Person findOneByName(String name) {
//        return peopleRepository.findByName(name).orElse(null);
//    }
//
//    @Transactional
//    public void save(Person person) {
//        peopleRepository.save(person);
//    }
//
//    @Transactional
//    public void update(int id, Person person) {
//        person.setId(id);
//        peopleRepository.save(person);
//    }
//
//    @Transactional
//    public void delete(int id) {
//        peopleRepository.deleteById(id);
//    }
