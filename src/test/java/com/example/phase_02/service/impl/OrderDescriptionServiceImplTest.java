package com.example.phase_02.service.impl;

import com.example.phase_02.entity.OrderDescription;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;



@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderDescriptionServiceImplTest {


    @Autowired
    OrderDescriptionServiceImpl orderDescriptionService;

    private OrderDescription orderDescription;
    private OrderDescription secondOrderDescription;

    @BeforeEach
    public void createEntities(){

        orderDescription = OrderDescription.builder()
                .customerSuggestedPrice(110000)
                .address("Tehran")
                .taskDetails("Cleaning house")
                .customerDesiredDateAndTime(LocalDateTime.of(2023,10,17,15,30))
                .build();
        secondOrderDescription = OrderDescription.builder()
                .customerSuggestedPrice(110000)
                .address("Isfahan")
                .taskDetails("Decorating salon")
                .customerDesiredDateAndTime(LocalDateTime.of(2023,10,20,12,15))
                .build();
    }

    @Test
    @Order(1)
    public void saveOrderDescription(){
        orderDescriptionService.saveOrUpdate(orderDescription);
        Assertions.assertNotEquals(0,orderDescription.getId());
    }

    @Test
    @Order(2)
    public void deleteOrderDescription(){
        orderDescriptionService.saveOrUpdate(orderDescription);
        long id = orderDescription.getId();
        orderDescriptionService.delete(orderDescription);
        Assertions.assertNull(orderDescriptionService.findById(id));
    }

    @Test
    @Order(3)
    public void findOrderDescriptionById(){
        orderDescriptionService.saveOrUpdate(orderDescription);
        Assertions.assertNotNull(orderDescriptionService.findById(orderDescription.getId()));
    }

    @Test
    @Order(4)
    public void findAllOrderDescriptions(){
        orderDescriptionService.saveOrUpdate(orderDescription);
        orderDescriptionService.saveOrUpdate(secondOrderDescription);
        Assertions.assertTrue(orderDescriptionService.findAll().size()>=2);
    }

}