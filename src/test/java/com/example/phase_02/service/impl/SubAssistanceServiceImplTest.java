package com.example.phase_02.service.impl;

import com.example.phase_02.entity.Assistance;
import com.example.phase_02.entity.Customer;
import com.example.phase_02.entity.Manager;
import com.example.phase_02.entity.SubAssistance;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SubAssistanceServiceImplTest {

    public static int counter = 0;

    @Autowired
    ManagerServiceImpl managerService;
    @Autowired
    CustomerServiceImpl customerService;
    @Autowired
    SubAssistanceServiceImpl subAssistanceService;
    @Autowired
    AssistanceServiceImpl assistanceService;

    private Customer customer;
    private Assistance assistance;
    private Assistance secondAssistance;
    private SubAssistance subAssistance;
    private SubAssistance secondSubAssistance;
    private Manager manager;


    @BeforeEach
    public void createEntities(){
        counter ++;

        customer = Customer.builder()
                .firstName("ali")
                .lastName("mohammadi")
                .email("ali" + counter + "@gmail.com")
                .credit(400000)
                .username("ali" + counter)
                .password("ali12345")
                .registrationDate(LocalDateTime.now())
                .build();
        assistance = Assistance.builder()
                .title("Cleaning" + counter)
                .build();
        secondAssistance = Assistance.builder()
                .title("Decorating" + counter)
                .build();
        subAssistance = SubAssistance.builder()
                .assistance(assistance)
                .title("house cleaning" + counter)
                .basePrice(100000)
                .about("All house cleaning services")
                .technicians(new ArrayList<>())
                .build();
        secondSubAssistance = SubAssistance.builder()
                .assistance(secondAssistance)
                .title("decoration" + counter)
                .basePrice(100000)
                .about("Modern decorating")
                .technicians(new ArrayList<>())
                .build();
        manager = Manager.builder()
                .firstName("amirhossein")
                .lastName("ahmadi")
                .email("amirhossein" + counter + "@gmail.com")
                .username("amirhossein" + counter)
                .password("amir1234")
                .registrationDate(LocalDateTime.now())
                .build();

    }

    @Test
    @Order(1)
    public void saveSubAssistance(){
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        Assertions.assertNotEquals(0,subAssistance.getId());
    }

    @Test
    @Order(2)
    public void deleteSubAssistance(){
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        subAssistanceService.delete(subAssistance);
        Assertions.assertNull(subAssistanceService.findById(subAssistance.getId()));
    }

    @Test
    @Order(3)
    public void findSubAssistanceById(){
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        Assertions.assertNotNull(assistanceService.findById(subAssistance.getId()));
    }

    @Test
    @Order(4)
    public void findAllSubAssistances(){
        assistanceService.saveOrUpdate(assistance);
        assistanceService.saveOrUpdate(secondAssistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        subAssistanceService.saveOrUpdate(secondSubAssistance);
        Assertions.assertTrue(subAssistanceService.findAll().size()>=2);
    }

    @Test
    @Order(5)
    public void findSubAssistanceByTitleAndRealatedAssistance(){
        assistanceService.saveOrUpdate(assistance);
        assistanceService.saveOrUpdate(secondAssistance);
        subAssistanceService.saveOrUpdate(subAssistance);

        Assertions.assertNotNull(subAssistanceService.findSubAssistance(subAssistance.getTitle(), assistance));
        Assertions.assertNull(subAssistanceService.findSubAssistance(subAssistance.getTitle(), secondAssistance));
    }

    @Test
    @Order(6)
    public void onlyManagerCanAddASubAssistance(){
        customerService.saveOrUpdate(customer);
        managerService.saveOrUpdate(manager);
        assistanceService.saveOrUpdate(assistance);
        secondSubAssistance.setAssistance(assistance);

        subAssistanceService.addSubAssistance(customer.getUsername(), assistance.getTitle(),subAssistance.getTitle(),120000L,"customer added this");
        subAssistanceService.addSubAssistance(manager.getUsername(), assistance.getTitle(),secondSubAssistance.getTitle(),120000L,"manager added this");

        Assertions.assertNull(subAssistanceService.findSubAssistance(subAssistance.getTitle(), assistance));
        Assertions.assertNotNull(subAssistanceService.findSubAssistance(secondSubAssistance.getTitle(), assistance));
    }

    @Test
    @Order(7)
    public void anyOneCanSeeSubAssistances(){
        assistanceService.saveOrUpdate(assistance);
        assistanceService.saveOrUpdate(secondAssistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        subAssistanceService.saveOrUpdate(secondSubAssistance);
        managerService.saveOrUpdate(manager);
        customerService.saveOrUpdate(customer);

        Assertions.assertNotNull(subAssistanceService.showSubAssistances(customer.getUsername()));
        Assertions.assertNotNull(subAssistanceService.showSubAssistances(manager.getUsername()));
    }

    @Test
    @Order(8)
    public void managerCanChangeDescriptionOfASubAssistance(){
        assistanceService.saveOrUpdate(assistance);
        assistanceService.saveOrUpdate(secondAssistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        subAssistanceService.saveOrUpdate(secondSubAssistance);
        managerService.saveOrUpdate(manager);
        customerService.saveOrUpdate(customer);

        subAssistanceService.changeDescription(customer.getUsername(),assistance.getTitle(),subAssistance.getTitle(),"changed by customer");
        subAssistanceService.changeDescription(manager.getUsername(),secondAssistance.getTitle(),secondSubAssistance.getTitle(),"changed by manager");

        Assertions.assertNotEquals("changed by customer",subAssistanceService.findSubAssistance(subAssistance.getTitle(), assistance).getAbout());
        Assertions.assertEquals("changed by manager",subAssistanceService.findSubAssistance(secondSubAssistance.getTitle(), secondAssistance).getAbout());
    }

    @Test
    @Order(9)
    public void manageCanChangeBasePriceOfASubAssistance(){
        assistanceService.saveOrUpdate(assistance);
        assistanceService.saveOrUpdate(secondAssistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        subAssistanceService.saveOrUpdate(secondSubAssistance);
        managerService.saveOrUpdate(manager);
        customerService.saveOrUpdate(customer);

        subAssistanceService.changeBasePrice(customer.getUsername(),assistance.getTitle(),subAssistance.getTitle(),120000);
        subAssistanceService.changeBasePrice(manager.getUsername(),secondAssistance.getTitle(),secondSubAssistance.getTitle(),120000);

        Assertions.assertNotEquals(120000,subAssistanceService.findSubAssistance(subAssistance.getTitle(), assistance).getBasePrice());
        Assertions.assertEquals(120000,subAssistanceService.findSubAssistance(secondSubAssistance.getTitle(), secondAssistance).getBasePrice());
    }
}