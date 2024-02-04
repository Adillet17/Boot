package com.controllers;

import com.DTO.AuthenticationDTO;
import com.DTO.PersonDTO;
import com.models.Person;
import com.services.RegistrationService;
import com.util.PersonValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final RegistrationService registrationService;
    private final PersonValidator personValidator;
    private final com.security.JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationDTO authenticationDTO;

    // Конструктор для внедрения зависимостей
    public AuthController(RegistrationService registrationService, PersonValidator personValidator
            , com.security.JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager, AuthenticationDTO authenticationDTO) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager= authenticationManager;
        this.authenticationDTO = authenticationDTO;
    }

    // Эндпоинт для отображения страницы логина
    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }

    // Эндпоинт для отображения страницы регистрации
    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person){
        return "auth/registration";
    }

    // Эндпоинт для выполнения регистрации
    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody @Valid com.DTO.PersonDTO personDTO,
                                                        BindingResult bindingResult){
        // Конвертация DTO в сущность Person
        Person person = cobvertToPerson(personDTO);

// Валидация полученных данных
        personValidator.validate(person, bindingResult);

        // Если есть ошибки валидации, возвращаем сообщение об ошибке
        if (bindingResult.hasErrors())
            return Map.of("message", "Ошибка!");

// Регистрация пользователя
        registrationService.register(person);

        // Генерация JWT-токена и возвращение в ответе
        String token = jwtUtil.generatedToken(person.getUsername());
        return Map.of("jwt-token", token);
    }

    // Эндпоинт для выполнения входа (аутентификации)
    @PostMapping("/login")
    public Map<String ,String > performLogin(@RequestBody com.DTO.AuthenticationDTO authenticationDTO){
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());
        try{
            // Аутентификация пользователя
            authenticationManager.authenticate(authInputToken);
        }catch (BadCredentialsException e){
            // В случае ошибки аутентификации возвращаем сообщение об ошибке
            return Map.of("message", "Incorrect credentials");
        }
// Генерация JWT-токена и возвращение в ответе
        String token = jwtUtil.generatedToken(authenticationDTO.getUsername());
        return Map.of("jwt-token", token);
    }

    // Метод для конвертации PersonDTO в Person
    public Person cobvertToPerson(@Valid PersonDTO personDTO){
        return this.modelMapper.map(personDTO, Person.class);
    }

    // Эндпоинт для активации пользователя по коду
    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        // Активация пользователя
    boolean isActivated = registrationService.activateUser(code);

        // Установка сообщения в модель
    if (isActivated)
        model.addAttribute("message", "User successfully activated");
    else
        model.addAttribute("message", "Activation code is not found");

        // Возвращение имени представления (view)
        return "login";
    }
}
