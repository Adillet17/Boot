package com.controllers;

import com.models.Person;
import com.services.PeopleService;
import com.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;
    private PersonValidator validator;

    // Конструктор для внедрения зависимости PeopleService
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    // Эндпоинт для отображения страницы со списком людей
    @GetMapping("/index")
    public String index(Model model) {
        // Добавление списка людей в модель
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }

    // Эндпоинт для отображения профиля человека по ID
    @GetMapping("/{id}")
    public String getPerson(@PathVariable("id") int id, Model model) {
        // Получение человека по ID
        Person person = peopleService.findOneById(id);
        // Добавление человека в модель
        model.addAttribute("person", person);
        return "people/profile";
    }

    // Эндпоинт для отображения страницы создания нового человека
    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    // Эндпоинт для создания нового человека
    @PostMapping()
    public String createPerson(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult) {
        // Валидация данных
        validator.validate(person, bindingResult);

        // Если есть ошибки валидации, возвращаем страницу создания нового человека
        if (bindingResult.hasErrors())
            return "people/new";

        // Сохранение человека
        peopleService.save(person);

        // Редирект на страницу со списком людей
        return "redirect:/library/people";
    }
}
