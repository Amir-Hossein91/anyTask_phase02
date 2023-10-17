package com.example.phase_02.service.impl;

import com.example.phase_02.entity.Person;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


@SpringBootTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class PersonServiceImplTest {
    private int counter = 0;

    @Autowired
    private PersonServiceImpl personService;

    private Person firstPerson;
    private Person secondPerson;

    @BeforeEach
    public void createEntities (){
        counter ++;
        firstPerson = Person.builder()
            .firstName("amirhossein")
            .lastName("ahmadi")
            .email("amir"+counter+"@gmail.com")
            .password("amir1234")
            .registrationDate(LocalDateTime.now())
            .username("amir"+counter)
            .build();
        secondPerson = Person.builder()
                .firstName("ali")
                .lastName("rahimi")
                .email("ali"+counter+"@gmail.com")
                .password("ali12345")
                .registrationDate(LocalDateTime.now())
                .username("ali"+counter)
                .build();

    }

    @Test
    @Order(1)
    public void savePerson(){
        personService.saveOrUpdate(firstPerson);
        Assertions.assertFalse(firstPerson.getId()==0);
    }

    @Test
    @Order(2)
    public void allUsersCanChangeTheirPassword(){
        personService.saveOrUpdate(firstPerson);
        personService.changePassword(firstPerson.getUsername(), firstPerson.getPassword(), "amir9876");
        Assertions.assertEquals("amir9876",personService.findByUsername(firstPerson.getUsername()).getPassword());
    }

    @Test
    @Order(3)
    public void deletePerson(){
        personService.saveOrUpdate(firstPerson);
        personService.delete(firstPerson);
        Assertions.assertNull(personService.findByUsername(firstPerson.getUsername()));
    }

    @Test
    @Order(4)
    public void findPersonById(){
        personService.saveOrUpdate(firstPerson);
        Assertions.assertNotNull(personService.findById(firstPerson.getId()));
    }

    @Test
    @Order(5)
    public void findAllPersons(){
        personService.saveOrUpdate(firstPerson);
        personService.saveOrUpdate(secondPerson);
        Assertions.assertEquals(2,personService.findAll().size());
    }

}