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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TechnicianSuggestionServiceImplTest {

    public static int counter = 0;

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
    private Customer customer;
    private Manager manager;
    private Assistance assistance;
    private SubAssistance subAssistance;
    private Order order;
    private Technician firstTechnician;
    private Technician secondTechnician;
    private TechnicianSuggestion firstTechnicianSuggestion;
    private TechnicianSuggestion secondTechnicianSuggestion;

    @BeforeEach
    public void createEntities(){

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
        firstTechnician = Technician.builder()
                .firstName("omid")
                .lastName("omidi")
                .email("omid"+counter+"@gmail.com")
//                .image(Files.readAllBytes(Path.of("C:\\Users\\AmirHossein\\IdeaProjects\\anyTask\\image_input\\technician_01.jpg")))
                .credit(0)
                .score(5)
                .username("omid" + counter)
                .password("omid1234")
                .isActive(true)
                .technicianStatus(TechnicianStatus.APPROVED)
                .build();
        secondTechnician = Technician.builder()
                .firstName("mohsen")
                .lastName("omidi")
                .email("mohsen"+counter+"@gmail.com")
//                .image(Files.readAllBytes(Path.of("C:\\Users\\AmirHossein\\IdeaProjects\\anyTask\\image_input\\technician_01.jpg")))
                .credit(0)
                .score(10)
                .username("mohsen" + counter)
                .password("omid1234")
                .isActive(true)
                .technicianStatus(TechnicianStatus.APPROVED)
                .build();
        firstTechnicianSuggestion = TechnicianSuggestion.builder()
                .technician(firstTechnician)
                .order(order)
                .DateAndTimeOfTechSuggestion(LocalDateTime.now())
                .taskEstimatedDuration(5)
                .techSuggestedDate(LocalDateTime.of(2023,10,18,15,30))
                .techSuggestedPrice(120000)
                .technician(firstTechnician)
                .build();
        secondTechnicianSuggestion = TechnicianSuggestion.builder()
                .technician(secondTechnician)
                .order(order)
                .DateAndTimeOfTechSuggestion(LocalDateTime.now())
                .taskEstimatedDuration(5)
                .techSuggestedDate(LocalDateTime.of(2023,10,18,15,30))
                .techSuggestedPrice(150000)
                .build();
        order = Order.builder()
                .subAssistance(subAssistance)
                .customer(customer)
                .orderRegistrationDateAndTime(LocalDateTime.now())
                .orderDescription(orderDescription)
                .orderStatus(OrderStatus.WAITING_FOR_TECHNICIANS_SUGGESTIONS)
                .technicianScore(1).build();
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    public void onlyManagerCanSeeTheListOfSuggestions(){
        technicianService.saveOrUpdate(firstTechnician);
        customerService.saveOrUpdate(customer);
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        orderService.saveOrUpdate(order);
        managerService.saveOrUpdate(manager);

        firstTechnicianSuggestion.setOrder(order);
        technicianSuggestionService.saveOrUpdate(firstTechnicianSuggestion);
        Assertions.assertTrue(technicianSuggestionService.showAllSuggestions(customer.getUsername()).isEmpty());
        Assertions.assertFalse(technicianSuggestionService.showAllSuggestions(manager.getUsername()).isEmpty());

    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void saveSuggestion(){
        technicianService.saveOrUpdate(firstTechnician);
        customerService.saveOrUpdate(customer);
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        orderService.saveOrUpdate(order);

        technicianSuggestionService.saveOrUpdate(firstTechnicianSuggestion);
        Assertions.assertTrue(firstTechnicianSuggestion.getId()!=0);

    }

    @Test
    @org.junit.jupiter.api.Order(3)
    public void deleteSuggestion(){
        technicianService.saveOrUpdate(firstTechnician);
        customerService.saveOrUpdate(customer);
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        orderService.saveOrUpdate(order);

        technicianSuggestionService.saveOrUpdate(firstTechnicianSuggestion);
        technicianSuggestionService.delete(firstTechnicianSuggestion);
        Assertions.assertNull(technicianSuggestionService.findById(firstTechnician.getId()));
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    public void findSuggestionsByid(){
        technicianService.saveOrUpdate(firstTechnician);
        customerService.saveOrUpdate(customer);
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        orderService.saveOrUpdate(order);

        technicianSuggestionService.saveOrUpdate(firstTechnicianSuggestion);
        Assertions.assertNotNull(technicianSuggestionService.findById(firstTechnicianSuggestion.getId()));
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    public void findAllSuggestions(){
        technicianService.saveOrUpdate(firstTechnician);
        customerService.saveOrUpdate(customer);
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        orderService.saveOrUpdate(order);

        technicianSuggestionService.saveOrUpdate(firstTechnicianSuggestion);
        Assertions.assertNotNull(technicianSuggestionService.findAll());
        Assertions.assertFalse(technicianSuggestionService.findAll().isEmpty());
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    public void customerCanOrderRelatedSuggestionsByPrice(){
        technicianService.saveOrUpdate(firstTechnician);
        technicianService.saveOrUpdate(secondTechnician);
        customerService.saveOrUpdate(customer);
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        orderService.saveOrUpdate(order);

        firstTechnicianSuggestion.setOrder(order);
        secondTechnicianSuggestion.setOrder(order);
        technicianSuggestionService.saveOrUpdate(firstTechnicianSuggestion);
        technicianSuggestionService.saveOrUpdate(secondTechnicianSuggestion);

        List<TechnicianSuggestionDTO> result = technicianSuggestionService.getSuggestionsOrderedByPrice(order);
        Assertions.assertTrue(result.get(0).getSuggestedPrice()<= result.get(1).getSuggestedPrice());

    }

    @Test
    @org.junit.jupiter.api.Order(7)
    public void customerCanOrderRelateSuggestionsByScore(){
        technicianService.saveOrUpdate(firstTechnician);
        technicianService.saveOrUpdate(secondTechnician);
        customerService.saveOrUpdate(customer);
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        orderService.saveOrUpdate(order);

        firstTechnicianSuggestion.setOrder(order);
        secondTechnicianSuggestion.setOrder(order);
        technicianSuggestionService.saveOrUpdate(firstTechnicianSuggestion);
        technicianSuggestionService.saveOrUpdate(secondTechnicianSuggestion);

        List<TechnicianSuggestionDTO> result = technicianSuggestionService.getSuggestionsOrderedByScore(order);
        Assertions.assertTrue(result.get(0).getTechnicianScore()>= result.get(1).getTechnicianScore());
    }
}