package com.example.phase_02.service.impl;

import com.example.phase_02.entity.Assistance;
import com.example.phase_02.entity.Customer;
import com.example.phase_02.entity.Manager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AssistanceServiceImplTest {

    public static int counter = 0;

    @Autowired
    AssistanceServiceImpl assistanceService;
    @Autowired
    ManagerServiceImpl managerService;
    @Autowired
    CustomerServiceImpl customerService;

    private Assistance assistance;
    private Assistance secondAssistance;
    private Manager manager;
    private Customer customer;

    @BeforeEach
    public void createEntities(){
        counter ++;

        assistance = Assistance.builder()
                .title("Cleaning"+counter)
                .build();
        secondAssistance = Assistance.builder()
                .title("maintening"+counter)
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
    }

    @Test
    @Order(1)
    public void saveAssistance(){
        assistanceService.saveOrUpdate(assistance);
        Assertions.assertNotEquals(0,assistance.getId());
    }

    @Test
    @Order(2)
    public void deleteAssistance(){
        assistanceService.saveOrUpdate(assistance);
        assistanceService.delete(assistance);
        Assertions.assertNull(assistanceService.findById(assistance.getId()));
    }

    @Test
    @Order(3)
    public void findAssistanceById(){
        assistanceService.saveOrUpdate(assistance);
        Assertions.assertNotNull(assistanceService.findById(assistance.getId()));
    }

    @Test
    @Order(4)
    public void finaAllAssistances(){
        assistanceService.saveOrUpdate(assistance);
        assistanceService.saveOrUpdate(secondAssistance);
        Assertions.assertTrue(assistanceService.findAll().size()>=2);
    }

    @Test
    @Order(5)
    public void findAssistanceByTitle(){
        assistanceService.saveOrUpdate(assistance);
        Assertions.assertNotNull(assistanceService.findAssistance(assistance.getTitle()));
    }

    @Test
    @Order(6)
    public void managerCanAddAssistance(){
        managerService.saveOrUpdate(manager);
        customerService.saveOrUpdate(customer);
        assistanceService.addAssistance(customer.getUsername(),assistance);
        assistanceService.addAssistance(manager.getUsername(),secondAssistance);

        Assertions.assertEquals(0,assistance.getId());
        Assertions.assertNotEquals(0,secondAssistance.getId());

    }

    @Test
    @Order(7)
    public void anyOneCanSeeTheListOfAssistances(){
        assistanceService.saveOrUpdate(assistance);
        assistanceService.saveOrUpdate(secondAssistance);
        managerService.saveOrUpdate(manager);
        customerService.saveOrUpdate(customer);

        List<String> fetchedByManager = assistanceService.seeAssistances(manager.getUsername());
        List<String> fetchedByCustomer = assistanceService.seeAssistances(customer.getUsername());

        Assertions.assertFalse(fetchedByManager.isEmpty());
        Assertions.assertFalse(fetchedByCustomer.isEmpty());
    }

}