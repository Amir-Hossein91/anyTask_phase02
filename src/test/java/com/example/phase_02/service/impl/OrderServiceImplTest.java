package com.example.phase_02.service.impl;

import com.example.phase_02.entity.*;
import com.example.phase_02.entity.enums.OrderStatus;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


@SpringBootTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class OrderServiceImplTest {

    @Autowired
    OrderServiceImpl orderService;
    @Autowired
    CustomerServiceImpl customerService;
    @Autowired
    SubAssistanceServiceImpl subAssistanceService;
    @Autowired
    AssistanceServiceImpl assistanceService;

    @Test
    @org.junit.jupiter.api.Order(1)
    public void saveOrder(){
        OrderDescription orderDescription = OrderDescription.builder()
                .customerSuggestedPrice(50000)
                .address("Tehran")
                .taskDetails("Cleaning house")
                .customerDesiredDateAndTime(LocalDateTime.of(2023,10,17,15,30))
                .build();
        Customer customer = Customer.builder()
                .firstName("ali")
                .lastName("mohammadi")
                .email("ali3@gmail.com")
                .credit(400000)
                .username("ali3")
                .password("ali12345")
                .registrationDate(LocalDateTime.now())
                .build();
        Assistance assistance = Assistance.builder()
                .title("Cleaning")
                .build();
        SubAssistance subAssistance = SubAssistance.builder()
                .assistance(assistance)
                .title("house cleaning")
                .basePrice(100000)
                .about("All house cleaning services")
                .build();
        Order order = Order.builder()
                .orderStatus(OrderStatus.WAITING_FOR_TECHNICIANS_SUGGESTIONS)
                .orderDescription(orderDescription)
                .customer(customer)
                .subAssistance(subAssistance)
                .orderRegistrationDateAndTime(LocalDateTime.now())
                .technicianScore(1)
                .build();

        customerService.saveOrUpdate(customer);
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);

        orderService.saveOrUpdate(order);
    }
}