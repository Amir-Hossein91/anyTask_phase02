package com.example.phase_02.service;

import com.example.phase_02.entity.Person;
import com.example.phase_02.service.impl.PersonServiceImple;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class PersonServiceTest {

    @Autowired
    private PersonServiceImple personService;

    @Test
    @Order(1)
    public void savePerson(){
        Person person = Person.builder()
                .firstName("amirhossein")
                .lastName("ahmadi")
                .email("amir@gmail.com")
                .password("amir1234")
                .registrationDate(LocalDateTime.now())
                .username("amir")
                .build();
        personService.saveOrUpdate(person);
    }


}