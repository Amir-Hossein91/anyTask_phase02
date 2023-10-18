package com.example.phase_02.service.impl;

import com.example.phase_02.entity.*;
import com.example.phase_02.entity.Order;
import com.example.phase_02.entity.dto.TechnicianSuggestionDTO;
import com.example.phase_02.entity.enums.OrderStatus;
import com.example.phase_02.entity.enums.TechnicianStatus;
import com.example.phase_02.service.OrderDescriptionService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerServiceImplTest {

    private static int counter = 0;

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
    @Autowired
    TechnicianSuggestionServiceImpl technicianSuggestionService;

    private OrderDescription orderDescription;
    private Customer firstCustomer;
    private Customer secondCustomer;
    private Manager manager;
    private Assistance assistance;
    private SubAssistance subAssistance;
    private Order order;
    private Technician technician;
    private Technician secondTechnician;
    private TechnicianSuggestion technicianSuggestion;
    private TechnicianSuggestion secondTechnicianSuggestion;

    @BeforeEach
    public void makeEntities() throws IOException {
        counter++;
        orderDescription = OrderDescription.builder()
                .customerSuggestedPrice(110000)
                .address("Tehran")
                .taskDetails("Cleaning house")
                .customerDesiredDateAndTime(LocalDateTime.of(2023, 10, 17, 15, 30))
                .build();
        firstCustomer = Customer.builder()
                .firstName("ali")
                .lastName("mohammadi")
                .email("ali" + counter + "@gmail.com")
                .credit(400000)
                .username("ali" + counter)
                .password("ali12345")
                .registrationDate(LocalDateTime.now())
                .build();
        secondCustomer = Customer.builder()
                .firstName("mohammad")
                .lastName("mohammadi")
                .email("mohammad" + counter + "@gmail.com")
                .credit(400000)
                .username("mohammad" + counter)
                .password("moh12345")
                .registrationDate(LocalDateTime.now())
                .build();
        assistance = Assistance.builder()
                .title("Cleaning" + counter)
                .build();
        subAssistance = SubAssistance.builder()
                .assistance(assistance)
                .title("house cleaning" + counter)
                .basePrice(100000)
                .about("All house cleaning services")
                .technicians(new ArrayList<>(List.of()))
                .build();
        manager = Manager.builder()
                .firstName("amirhossein")
                .lastName("ahmadi")
                .email("amirhossein" + counter + "@gmail.com")
                .username("amirhossein" + counter)
                .password("amir1234")
                .registrationDate(LocalDateTime.now())
                .build();
        order = Order.builder()
                .subAssistance(subAssistance)
                .customer(firstCustomer)
                .orderRegistrationDateAndTime(LocalDateTime.now())
                .orderDescription(orderDescription)
                .orderStatus(OrderStatus.WAITING_FOR_TECHNICIANS_SUGGESTIONS)
                .technicianSuggestions(new ArrayList<>(List.of()))
                .technicianScore(1).build();
        technician = Technician.builder()
                .firstName("omid")
                .lastName("omidi")
                .email("omid" + counter + "@gmail.com")
//                .image(Files.readAllBytes(Path.of("C:\\Users\\AmirHossein\\IdeaProjects\\anyTask\\image_input\\technician_01.jpg")))
                .credit(0)
                .score(5)
                .username("omid" + counter)
                .password("omid1234")
                .isActive(true)
                .technicianStatus(TechnicianStatus.APPROVED)
                .build();
        secondTechnician = Technician.builder()
                .firstName("rahim")
                .lastName("rahimi")
                .email("rahim" + counter + "@gmail.com")
//                .image(Files.readAllBytes(Path.of("C:\\Users\\AmirHossein\\IdeaProjects\\anyTask\\image_input\\technician_01.jpg")))
                .credit(0)
                .score(10)
                .username("rahim" + counter)
                .password("rahim123")
                .isActive(true)
                .technicianStatus(TechnicianStatus.APPROVED)
                .build();
        technicianSuggestion = TechnicianSuggestion.builder()
                .technician(technician)
                .order(order)
                .DateAndTimeOfTechSuggestion(LocalDateTime.now())
                .taskEstimatedDuration(5)
                .techSuggestedDate(LocalDateTime.of(2023, 10, 18, 15, 30))
                .techSuggestedPrice(120000)
                .build();
        secondTechnicianSuggestion = TechnicianSuggestion.builder()
                .technician(secondTechnician)
                .order(order)
                .DateAndTimeOfTechSuggestion(LocalDateTime.now())
                .taskEstimatedDuration(5)
                .techSuggestedDate(LocalDateTime.of(2023, 10, 18, 15, 30))
                .techSuggestedPrice(140000)
                .build();
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    public void saveCustomer(){
        customerService.saveOrUpdate(firstCustomer);
        Assertions.assertNotEquals(0, firstCustomer.getId());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void deleteCustomer(){
        customerService.saveOrUpdate(firstCustomer);
        customerService.delete(firstCustomer);
        Assertions.assertNull(customerService.findByUsername(firstCustomer.getUsername()));
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    public void findCustomerById(){
        customerService.saveOrUpdate(firstCustomer);
        Assertions.assertNotNull(customerService.findById(firstCustomer.getId()));
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    public void findCustomerByUsername(){
        customerService.saveOrUpdate(firstCustomer);
        Assertions.assertNotNull(customerService.findByUsername(firstCustomer.getUsername()));
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    public void findAllCustomers(){
        customerService.saveOrUpdate(firstCustomer);
        customerService.saveOrUpdate(secondCustomer);
        Assertions.assertTrue(customerService.findAll().size()>=2);
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    public void onlyManagerCanSeeAllCustomers(){
        managerService.saveOrUpdate(manager);
        customerService.saveOrUpdate(firstCustomer);
        List<String> fetchedByManager = customerService.showAllCustomers(manager.getUsername());
        List<String> fetchedByCustomer = customerService.showAllCustomers(firstCustomer.getUsername());
        Assertions.assertTrue(fetchedByCustomer.isEmpty());
        Assertions.assertFalse(fetchedByManager.isEmpty());
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    @Transactional
    public void customerCanSeeListOfRelatedOrders(){
        customerService.saveOrUpdate(firstCustomer);
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        orderDescriptionService.saveOrUpdate(orderDescription);
        orderService.saveOrUpdate(order);

        Assertions.assertFalse(customerService.seeOrdersOf(firstCustomer.getUsername()).isEmpty());
    }

    @Test
    @org.junit.jupiter.api.Order(8)
    @Transactional
    public void customerCanOnlySeeSuggestionsOfOrdersWhichAreRelatedToHim(){
        customerService.saveOrUpdate(firstCustomer);
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        orderDescriptionService.saveOrUpdate(orderDescription);
        technicianService.saveOrUpdate(technician);
        technicianSuggestionService.saveOrUpdate(technicianSuggestion);
        order.getTechnicianSuggestions().add(technicianSuggestion);
        orderService.saveOrUpdate(order);


        customerService.saveOrUpdate(secondCustomer);

        Assertions.assertTrue(customerService.seeTechnicianSuggestionsOrderedByScore(secondCustomer.getUsername(), order.getId()).isEmpty());
        Assertions.assertFalse(customerService.seeTechnicianSuggestionsOrderedByScore(firstCustomer.getUsername(), order.getId()).isEmpty());
    }

    @Test
    @org.junit.jupiter.api.Order(9)
    @Transactional
    public void customerCanSeeTheTechnicianSuggestionsOrderedByPrice(){
        customerService.saveOrUpdate(firstCustomer);
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        orderDescriptionService.saveOrUpdate(orderDescription);
        technicianService.saveOrUpdate(technician);
        orderService.saveOrUpdate(order);
        technicianSuggestionService.saveOrUpdate(technicianSuggestion);
        technicianService.saveOrUpdate(secondTechnician);
        technicianSuggestionService.saveOrUpdate(secondTechnicianSuggestion);
        order.getTechnicianSuggestions().add(technicianSuggestion);
        order.getTechnicianSuggestions().add(secondTechnicianSuggestion);
        orderService.saveOrUpdate(order);

        List<TechnicianSuggestionDTO> suggestions = customerService.seeTechnicianSuggestionsOrderedByPrice(firstCustomer.getUsername(), order.getId());
        Assertions.assertTrue(suggestions.get(0).getSuggestedPrice()<=suggestions.get(1).getSuggestedPrice());
    }

    @Test
    @org.junit.jupiter.api.Order(10)
    @Transactional
    public void customerCanSeeTheTechnicianSuggestionsOrderedByTechnicianScore(){
        customerService.saveOrUpdate(firstCustomer);
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        orderDescriptionService.saveOrUpdate(orderDescription);
        technicianService.saveOrUpdate(technician);
        orderService.saveOrUpdate(order);
        technicianSuggestionService.saveOrUpdate(technicianSuggestion);
        technicianService.saveOrUpdate(secondTechnician);
        technicianSuggestionService.saveOrUpdate(secondTechnicianSuggestion);
        order.getTechnicianSuggestions().add(technicianSuggestion);
        order.getTechnicianSuggestions().add(secondTechnicianSuggestion);
        orderService.saveOrUpdate(order);

        List<TechnicianSuggestionDTO> suggestions = customerService.seeTechnicianSuggestionsOrderedByScore(firstCustomer.getUsername(), order.getId());
        Assertions.assertTrue(suggestions.get(0).getTechnicianScore()>=suggestions.get(1).getTechnicianScore());
    }

    @Test
    @org.junit.jupiter.api.Order(11)
    @Transactional
    public void customerCanChooseARelatedTechnicianSuggestion(){
        customerService.saveOrUpdate(firstCustomer);
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        orderDescriptionService.saveOrUpdate(orderDescription);
        technicianService.saveOrUpdate(technician);
        orderService.saveOrUpdate(order);
        technicianSuggestionService.saveOrUpdate(technicianSuggestion);
        technicianService.saveOrUpdate(secondTechnician);
        technicianSuggestionService.saveOrUpdate(secondTechnicianSuggestion);
        order.getTechnicianSuggestions().add(technicianSuggestion);
        order.getTechnicianSuggestions().add(secondTechnicianSuggestion);
        orderService.saveOrUpdate(order);

        customerService.chooseSuggestion(firstCustomer.getUsername(), order.getId(), technicianSuggestion.getId());
        Assertions.assertNotNull(order.getTechnician());
        Assertions.assertEquals(OrderStatus.TECHNICIAN_IS_ON_THE_WAY,order.getOrderStatus());
    }

    @Test
    @org.junit.jupiter.api.Order(12)
    @Transactional
    public void customerCanMarkAnOrderAsStarted(){
        customerCanChooseARelatedTechnicianSuggestion();

        customerService.markOrderAsStarted(firstCustomer.getUsername(), order.getId(), technicianSuggestion.getId());

        Assertions.assertEquals(OrderStatus.STARTED,order.getOrderStatus());
    }

    @Test
    @org.junit.jupiter.api.Order(13)
    @Transactional
    public void customerCanMarkAnOrderAsFinished(){
        customerCanMarkAnOrderAsStarted();

        customerService.markOrderAsFinished(firstCustomer.getUsername(), order.getId(), technicianSuggestion.getId());

        Assertions.assertEquals(OrderStatus.FINISHED,order.getOrderStatus());
    }

    @Test
    @org.junit.jupiter.api.Order(14)
    @Transactional
    public void customerCanNotMarkOrderAsFinishedIfNotStarted(){
        customerCanChooseARelatedTechnicianSuggestion();

        customerService.markOrderAsFinished(firstCustomer.getUsername(), order.getId(), technicianSuggestion.getId());

        Assertions.assertNotEquals(OrderStatus.FINISHED,order.getOrderStatus());
    }

    @Test
    @org.junit.jupiter.api.Order(15)
    @Transactional
    public void customerCanPayThePriceOfOrderAfterMarkedItAsFinished(){
        customerCanMarkAnOrderAsFinished();
        customerService.payThePrice(firstCustomer.getUsername(), order.getId());
        Assertions.assertEquals(280000,firstCustomer.getCredit());
    }

    @Test
    @org.junit.jupiter.api.Order(16)
    @Transactional
    public void customerCanScoreTechnicianAfterTheOrderWasFinished(){
        customerCanMarkAnOrderAsFinished();

        customerService.scoreTheTechnician(firstCustomer.getUsername(), order.getId(), 3, "not so good");

        Assertions.assertEquals(3,order.getTechnicianScore());
    }



}