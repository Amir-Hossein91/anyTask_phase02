package com.example.phase_02.service.impl;

import com.example.phase_02.entity.Customer;
import com.example.phase_02.entity.Manager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class ManagerServiceImplTest {
    private static int counter = 0;

    @Autowired
    ManagerServiceImpl managerService;

    private Manager manager;

    @BeforeEach
    public void createEntities(){
        counter ++;
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
    public void saveManager(){
        managerService.saveOrUpdate(manager);
        Assertions.assertFalse(manager.getId()==0);
    }

    @Test
    @Order(2)
    public void deleteManager(){
        managerService.saveOrUpdate(manager);
        managerService.delete(manager);
        Assertions.assertNull(managerService.findByUsername(manager.getUsername()));
    }

    @Test
    @Order(3)
    public void findManagerByUsername(){
        managerService.saveOrUpdate(manager);
        Manager fetchedManager = managerService.findByUsername(manager.getUsername());
        Assertions.assertTrue(fetchedManager.getId()!=0);
    }

    @Test
    @Order(4)
    public void findManagerById(){
        managerService.saveOrUpdate(manager);
        Assertions.assertNotNull(managerService.findById(manager.getId()));
    }

    @Test
    @Order(5)
    public void findAllManagers(){
        managerService.saveOrUpdate(manager);
        Assertions.assertTrue(managerService.findAll().size()>=1);
    }

    @Test
    @Order(6)
    public void checkIfThereIsAManager(){
        managerService.saveOrUpdate(manager);
        Assertions.assertTrue(managerService.doesManagerExist());
    }
}