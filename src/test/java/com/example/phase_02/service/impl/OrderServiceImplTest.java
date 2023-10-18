package com.example.phase_02.service.impl;

import com.example.phase_02.entity.*;
import com.example.phase_02.entity.Order;
import com.example.phase_02.entity.dto.OrderDTO;
import com.example.phase_02.entity.enums.OrderStatus;
import com.example.phase_02.entity.enums.TechnicianStatus;
import com.example.phase_02.service.OrderDescriptionService;
import com.example.phase_02.utility.ApplicationContext;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class OrderServiceImplTest {

    private static int counter =0;

    @Autowired
    OrderDescriptionService orderDescriptionService;
    @Autowired
    OrderServiceImpl orderService;
    @Autowired
    CustomerServiceImpl customerService;
    @Autowired
    SubAssistanceServiceImpl subAssistanceService;
    @Autowired
    AssistanceServiceImpl assistanceService;
    @Autowired
    ManagerServiceImpl managerService;
    @Autowired
    TechnicianServiceImpl technicianService;

    private OrderDescription orderDescription;
    private Customer customer;
    private Manager manager;
    private Assistance assistance;
    private SubAssistance subAssistance;
    private Order order;
    private Technician technician;
    private TechnicianSuggestion technicianSuggestion;

    @BeforeEach
    public void makeEntities() throws IOException {
        counter ++;
        orderDescription = OrderDescription.builder()
                .customerSuggestedPrice(110000)
                .address("Tehran")
                .taskDetails("Cleaning house")
                .customerDesiredDateAndTime(LocalDateTime.of(2023,10,17,15,30))
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
        assistance = Assistance.builder()
                .title("Cleaning"+counter)
                .build();
        subAssistance = SubAssistance.builder()
                .assistance(assistance)
                .title("house cleaning"+counter)
                .basePrice(100000)
                .about("All house cleaning services")
                .technicians(new ArrayList<>(List.of()))
                .build();
        manager = Manager.builder()
                .firstName("amirhossein")
                .lastName("ahmadi")
                .email("amirhossein"+counter+"@gmail.com")
                .username("amirhossein"+counter)
                .password("amir1234")
                .registrationDate(LocalDateTime.now())
                .build();
        order = Order.builder()
                .subAssistance(subAssistance)
                .customer(customer)
                .orderRegistrationDateAndTime(LocalDateTime.now())
                .orderDescription(orderDescription)
                .orderStatus(OrderStatus.WAITING_FOR_TECHNICIANS_SUGGESTIONS)
                .technicianScore(1).build();
        technician = Technician.builder()
                .firstName("omid")
                .lastName("omidi")
                .email("omid"+counter+"@gmail.com")
//                .image(Files.readAllBytes(Path.of("C:\\Users\\AmirHossein\\IdeaProjects\\anyTask\\image_input\\technician_01.jpg")))
                .credit(0)
                .score(0)
                .username("omid" + counter)
                .password("omid1234")
                .isActive(true)
                .technicianStatus(TechnicianStatus.APPROVED)
                .build();
        technicianSuggestion = TechnicianSuggestion.builder()
                .technician(technician)
                .order(order)
                .DateAndTimeOfTechSuggestion(LocalDateTime.now())
                .taskEstimatedDuration(5)
                .techSuggestedDate(LocalDateTime.of(2023,10,18,15,30))
                .techSuggestedPrice(120000)
                .build();
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    public void saveOrder(){
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
        order = orderService.saveOrUpdate(order);
        Assertions.assertNotEquals(0,order.getId());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    @Transactional
    public void notShowOrdersToSomeOneNotManager(){
        customerService.saveOrUpdate(customer);
        System.out.println(orderService.showAllOrders(customer.getUsername()));
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    @Transactional
    public void showOrdersToManager(){
        managerService.saveOrUpdate(manager);
        System.out.println(orderService.showAllOrders(manager.getUsername()));
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    @Transactional
    public void CustomersCanMakeOrders(){
        customerService.saveOrUpdate(customer);
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        orderService.makeOrder(customer.getUsername(), assistance.getTitle(), subAssistance.getTitle(),orderDescription);

    }

    @Test
    @org.junit.jupiter.api.Order(5)
    public void CanNotMakeOrdersIfNotCustomer(){
        managerService.saveOrUpdate(manager);
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        orderService.makeOrder(manager.getUsername(), assistance.getTitle(), subAssistance.getTitle(),orderDescription);
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    public void deleteOrder(){
        customerService.saveOrUpdate(customer);
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        orderService.saveOrUpdate(order);
        orderService.delete(order);
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    public void deleteInvalidOrderThrowsRuntimeExcepton(){
        Order invalidOrder = Order.builder()
                .customer(customer)
                .orderDescription(orderDescription)
                        .build();

        orderService.delete(invalidOrder);
    }

    @Test
    @org.junit.jupiter.api.Order(8)
    public void findById(){
        customerService.saveOrUpdate(customer);
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        orderService.saveOrUpdate(order);
        orderService.findById(order.getId());
        Assertions.assertNotNull(order.getId());
    }

    @Test
    @org.junit.jupiter.api.Order(9)
    public void getOrdersOfSpecificTechnicianWithNoOrders(){
        technicianService.saveOrUpdate(technician);
        List<OrderDTO> orders = orderService.findRelatedOrders(technician);
        Assertions.assertTrue(orders.isEmpty());
    }

    @Test
    @org.junit.jupiter.api.Order(10)
    public void getOrdersOfSpecificTechnicianWithSomeSubAssistanceAssigned(){

        customerService.saveOrUpdate(customer);
        assistanceService.saveOrUpdate(assistance);
        subAssistance.getTechnicians().add(technician);
        technicianService.saveOrUpdate(technician);
        subAssistanceService.saveOrUpdate(subAssistance);
        order = orderService.saveOrUpdate(order);
        Assertions.assertFalse(orderService.findRelatedOrders(technician).isEmpty());
    }

    @Test
    @org.junit.jupiter.api.Order(11)
    public void techniciansCanSendSuggestionToRelatedOrders(){
        customerService.saveOrUpdate(customer);
        assistanceService.saveOrUpdate(assistance);
        technicianService.saveOrUpdate(technician);
        subAssistance.getTechnicians().add(technician);
        subAssistanceService.saveOrUpdate(subAssistance);
        order = orderService.saveOrUpdate(order);
        orderService.sendTechnicianSuggestion(technician,order,technicianSuggestion);
    }

    @Test
    @org.junit.jupiter.api.Order(12)
    public void ordersCanBeFoundByCustomerObject(){
        customerService.saveOrUpdate(customer);
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        orderService.saveOrUpdate(order);
        Assertions.assertFalse(orderService.findByCustomer(customer).isEmpty());
    }



}