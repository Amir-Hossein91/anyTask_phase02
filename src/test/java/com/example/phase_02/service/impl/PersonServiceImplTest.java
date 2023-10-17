package com.example.phase_02.service.impl;

import com.example.phase_02.entity.Customer;
import com.example.phase_02.entity.Manager;
import com.example.phase_02.entity.Person;
import com.example.phase_02.entity.Technician;
import com.example.phase_02.entity.enums.TechnicianStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;


@SpringBootTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class PersonServiceImplTest {
    private static int counter = 0;

    @Autowired
    private PersonServiceImpl personService;
    @Autowired
    private ManagerServiceImpl managerService;

    private Person firstPerson;
    private Person secondPerson;
    private Customer customer;
    private Manager manager;
    private Manager secondManager;
    private Technician technician;

    @BeforeEach
    public void createEntities () throws IOException {
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
        customer = Customer.builder()
                .firstName("ali")
                .lastName("mohammadi")
                .email("ali"+counter+"@gmail.com")
                .credit(400000)
                .username("ali"+counter)
                .password("ali12345")
                .registrationDate(LocalDateTime.now())
                .build();
        manager = Manager.builder()
                .firstName("amirhossein")
                .lastName("ahmadi")
                .email("amirhossein"+counter+"@gmail.com")
                .username("amirhossein"+counter)
                .password("amir1234")
                .registrationDate(LocalDateTime.now())
                .build();
        secondManager = Manager.builder()
                .firstName("ahamad")
                .lastName("abbasi")
                .email("ahmad"+counter+"@gmail.com")
                .username("ahmad"+counter)
                .password("ahmad123")
                .registrationDate(LocalDateTime.now())
                .build();
        technician = Technician.builder()
                .firstName("omid")
                .lastName("omidi")
                .email("omid"+counter+"@gmail.com")
                .image(Files.readAllBytes(Path.of("C:\\Users\\AmirHossein\\IdeaProjects\\anyTask\\image_input\\technician_01.jpg")))
                .credit(0)
                .score(0)
                .username("omid" + counter)
                .password("omid1234")
                .isActive(true)
                .technicianStatus(TechnicianStatus.APPROVED)
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
        Assertions.assertTrue(personService.findAll().size()>=2);
    }

    @Test
    @Order(6)
    public void customerCanRegisterInApp(){
        personService.registerCustomer(customer);
        Assertions.assertFalse(customer.getId()==0);
    }

    @Test
    @Order(7)
    public void technicianCanRegisterInApp(){
        personService.registerTechnician(technician);
        Assertions.assertFalse(technician.getId()==0);
    }

    @Test
    @Order(8)
    public void managerCanBeSetForTheOrganization(){
        personService.registerManager(manager);
        Assertions.assertNotEquals(0, manager.getId());
    }

    @Test
    @Order(9)
    public void OnlyOneManagerCanBeSet(){
        personService.registerManager(manager);
        personService.registerManager(secondManager);
        Assertions.assertEquals(0, secondManager.getId());
    }

    @Test
    @Order(11)
    public void UsersCanNotLogInIfUsernameOrPasswordIsWrong(){
        personService.registerCustomer(customer);
        personService.login(customer.getUsername(), "AAAA1111");
        Assertions.assertFalse(PersonServiceImpl.isLoggedIn);
    }

    @Test
    @Order(10)
    public void usersCanLogInIfUsernameAndPasswordAreCorrect(){
        personService.registerCustomer(customer);
        personService.login((customer.getUsername()), customer.getPassword());
        Assertions.assertTrue(PersonServiceImpl.isLoggedIn);
    }

    @Test
    @Order(12)
    public void findPersonByUsername(){
        personService.registerCustomer(customer);
        Customer fetchedCustomer = (Customer) personService.findByUsername(customer.getUsername());
        Assertions.assertTrue(fetchedCustomer.getId()!=0);
    }

}