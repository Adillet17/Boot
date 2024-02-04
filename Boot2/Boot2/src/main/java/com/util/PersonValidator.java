package com.util;

import com.models.Person;
import com.services.PeopleService;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    public final PeopleService peopleService;

    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }


    @Override
    public boolean supports(Class<?> aClass){
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }

    public void validate(Object o, BindingResult errors){
            Person person = (Person)o;
            try {
                peopleService.loadUserByUsername(person.getUsername());
            }catch (Exception ignored){
                return;  //Все ок, пользователь не найден
            }
            errors.rejectValue("username", "", "Человек с таким именем уже существует");
    }
}
