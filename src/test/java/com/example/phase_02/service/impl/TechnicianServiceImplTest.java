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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TechnicianServiceImplTest {

    public static int counter;

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
    @Autowired
    PersonServiceImpl personService;

    private OrderDescription orderDescription;
    private OrderDescription secondOrderDescription;
    private Customer firstCustomer;
    private Customer secondCustomer;
    private Manager manager;
    private Assistance assistance;
    private SubAssistance subAssistance;
    private SubAssistance secondSubAssistance;
    private com.example.phase_02.entity.Order order;
    private com.example.phase_02.entity.Order secondOrder;
    private Technician technician;
    private Technician secondTechnician;
    private TechnicianSuggestion technicianSuggestion;
    private TechnicianSuggestion secondTechnicianSuggestion;

    @BeforeEach
    public void makeEntities() {
        counter++;
        orderDescription = OrderDescription.builder()
                .customerSuggestedPrice(110000)
                .address("Tehran")
                .taskDetails("Cleaning house")
                .customerDesiredDateAndTime(LocalDateTime.of(2023, 10, 17, 15, 30))
                .build();
        secondOrderDescription = OrderDescription.builder()
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
                .technicians(new ArrayList<>())
                .build();
        secondSubAssistance = SubAssistance.builder()
                .assistance(assistance)
                .title("decoration" + counter)
                .basePrice(100000)
                .about("All house cleaning services")
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
        order = Order.builder()
                .subAssistance(subAssistance)
                .customer(firstCustomer)
                .orderRegistrationDateAndTime(LocalDateTime.now())
                .orderDescription(orderDescription)
                .orderStatus(OrderStatus.WAITING_FOR_TECHNICIANS_SUGGESTIONS)
                .technicianSuggestions(new ArrayList<>())
                .technicianScore(1).build();
        secondOrder = Order.builder()
                .subAssistance(secondSubAssistance)
                .customer(secondCustomer)
                .orderRegistrationDateAndTime(LocalDateTime.now())
                .orderDescription(secondOrderDescription)
                .orderStatus(OrderStatus.WAITING_FOR_TECHNICIANS_SUGGESTIONS)
                .technicianSuggestions(new ArrayList<>())
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
                .subAssistances(new ArrayList<>())
                .technicianStatus(TechnicianStatus.APPROVED)
                .registrationDate(LocalDateTime.of(2022,6,12,14,25))
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
                .subAssistances(new ArrayList<>())
                .technicianStatus(TechnicianStatus.APPROVED)
                .registrationDate(LocalDateTime.of(2020,7,14,10,20))
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
    public void saveTechnician(){
        technicianService.saveOrUpdate(technician);
        Assertions.assertNotEquals(0,technician.getId());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void saveAListOfTechnicians(){
        List<Technician> technicians = new ArrayList<>(List.of(technician,secondTechnician));
        technicianService.saveOrUpdate(technicians);
        Assertions.assertNotEquals(0,technician.getId());
        Assertions.assertNotEquals(0,secondTechnician.getId());
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    public void deleteTechnician(){
        technicianService.saveOrUpdate(technician);
        technicianService.delete(technician);
        Assertions.assertNull(technicianService.findByUsername(technician.getUsername()));
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    public void findTechnicianById(){
        technicianService.saveOrUpdate(technician);
        Assertions.assertNotNull(technicianService.findById(technician.getId()));
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    public void findTechnicianByUsername(){
        technicianService.saveOrUpdate(technician);
        Assertions.assertNotNull(technicianService.findByUsername(technician.getUsername()));
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    public void findAllTechnicians(){
        technicianService.saveOrUpdate(technician);
        technicianService.saveOrUpdate(secondTechnician);
        Assertions.assertTrue(technicianService.findAll().size()>=2);
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    public void onlyManagerCanSeetheListOfAllTechnicians(){
        technicianService.saveOrUpdate(technician);
        technicianService.saveOrUpdate(secondTechnician);
        managerService.saveOrUpdate(manager);
        List<Technician> fetchedByTechnician = technicianService.showAllTechnicians(technician.getUsername());
        List<Technician> fetchedByManager = technicianService.showAllTechnicians(manager.getUsername());

        Assertions.assertTrue(fetchedByTechnician.isEmpty());
        Assertions.assertFalse(fetchedByManager.isEmpty());
    }

    @Test
    @org.junit.jupiter.api.Order(8)
    @Transactional
    public void managerCanSeeTheListOfTechniciansThatAreNotApprovedYet(){
        customerService.saveOrUpdate(firstCustomer);
        managerService.saveOrUpdate(manager);
        technician.setTechnicianStatus(TechnicianStatus.NEW);
        technicianService.saveOrUpdate(technician);
        technicianService.saveOrUpdate(secondTechnician);

        List<Technician> fetchedByCustomer = technicianService.seeUnapprovedTechnicians(firstCustomer.getUsername());
        List<Technician> fetchedByManager = technicianService.seeUnapprovedTechnicians(manager.getUsername());

        Assertions.assertNull(fetchedByCustomer);
        Assertions.assertFalse(fetchedByManager.isEmpty());

    }

    @Test
    @org.junit.jupiter.api.Order(9)
    public void managerCanSeeTheListOfTechniciansThatAreApprovedButAreDeactivatedNow(){
        customerService.saveOrUpdate(firstCustomer);
        managerService.saveOrUpdate(manager);
        technician.setActive(false);
        technicianService.saveOrUpdate(technician);
        technicianService.saveOrUpdate(secondTechnician);

        List<Technician> fetchedByCustomer = technicianService.seeDeactivatedTechnicians(firstCustomer.getUsername());
        List<Technician> fetchedByManager = technicianService.seeDeactivatedTechnicians(manager.getUsername());

        Assertions.assertNull(fetchedByCustomer);
        Assertions.assertFalse(fetchedByManager.isEmpty());
    }

    @Test
    @org.junit.jupiter.api.Order(10)
    public void technicianImageMustHaveJpgForamt() {
        Path invalidImagePath = Path.of("C:\\Users\\AmirHossein\\IdeaProjects\\anyTask\\image_input\\technician_01.png");
        Path validImagePath = Path.of("C:\\Users\\AmirHossein\\IdeaProjects\\anyTask\\image_input\\technician_01.jpg");

        Assertions.assertFalse(technicianService.validateImage(invalidImagePath));
        Assertions.assertTrue(technicianService.validateImage(validImagePath));
    }

    @Test
    @org.junit.jupiter.api.Order(11)
    public void technicianImageMustNotBeLargerThan300KB() {
        Path invalidImagePath = Path.of("C:\\Users\\AmirHossein\\IdeaProjects\\anyTask\\image_input\\hd.jpg");
        Path validImagePath = Path.of("C:\\Users\\AmirHossein\\IdeaProjects\\anyTask\\image_input\\technician_01.jpg");

        Assertions.assertFalse(technicianService.validateImage(invalidImagePath));
        Assertions.assertTrue(technicianService.validateImage(validImagePath));
    }

    @Test
    @org.junit.jupiter.api.Order(12)
    public void whenSavingATechnician_TheImageMustBeSavedInADefinedDirectory() throws IOException {
        Path input = ApplicationContext.inputPath;
        Path output = ApplicationContext.outputPath;
        personService.registerTechnician(technician,input,output);
        byte[] savedImage = Files.readAllBytes(output);
        Assertions.assertNotEquals(0,savedImage.length);
    }

    @Test
    @org.junit.jupiter.api.Order(13)
    @Transactional
    public void managerCanAssignTechnicianToSubAssistances(){
        managerService.saveOrUpdate(manager);
        assistanceService.saveOrUpdate(assistance);
        subAssistanceService.saveOrUpdate(subAssistance);
        technicianService.saveOrUpdate(technician);

        technicianService.addTechnicianToSubAssistance(manager.getUsername(),technician.getUsername(),subAssistance.getTitle(),assistance.getTitle());

        Assertions.assertFalse(subAssistance.getTechnicians().isEmpty());
    }
    @Test
    @org.junit.jupiter.api.Order(14)
    @Transactional
    public void managerCanRemoveATechnicianFromASubAssistance(){
        managerCanAssignTechnicianToSubAssistances();

        int beforeDeletionSize = subAssistance.getTechnicians().size();
        technicianService.removeTechnicianFromSubAssistance(manager.getUsername(),technician.getUsername(),subAssistance.getTitle(),assistance.getTitle());
        int afterDeletionSize = subAssistance.getTechnicians().size();

        Assertions.assertEquals(1,beforeDeletionSize - afterDeletionSize);
    }

    @Test
    @org.junit.jupiter.api.Order(15)
    @Transactional
    public void technicianCanSeeListOfOrdersRelatedToHim(){
        technicianService.saveOrUpdate(technician);
        assistanceService.saveOrUpdate(assistance);
        subAssistance.getTechnicians().add(technician);
        subAssistanceService.saveOrUpdate(subAssistance);
        subAssistanceService.saveOrUpdate(secondSubAssistance);
        customerService.saveOrUpdate(firstCustomer);
        customerService.saveOrUpdate(secondCustomer);
        orderService.saveOrUpdate(order);
        orderService.saveOrUpdate(secondOrder);

        List<OrderDTO> relatedOrders = technicianService.findRelatedOrders(technician.getUsername());
        boolean isSecondOrderRelated = false;
        for(OrderDTO o : relatedOrders){
            if(o.getOrderId() == secondOrder.getId()){
                isSecondOrderRelated = true;
                break;
            }
        }

        Assertions.assertFalse(isSecondOrderRelated);
        Assertions.assertFalse(relatedOrders.isEmpty());
    }

    @Test
    @org.junit.jupiter.api.Order(16)
    @Transactional
    public void technicianCanSendSuggestionToOrdersWhichAreRelatedToHim(){
        technicianCanSeeListOfOrdersRelatedToHim();

        technicianSuggestionService.saveOrUpdate(technicianSuggestion);
        secondTechnicianSuggestion.setTechnician(technician);
        secondTechnicianSuggestion.setOrder(secondOrder);
        technicianSuggestionService.saveOrUpdate(secondTechnicianSuggestion);

        technicianService.sendTechnicianSuggestion(technician.getUsername(),order.getId(),technicianSuggestion);
        technicianService.sendTechnicianSuggestion(technician.getUsername(),secondOrder.getId(),secondTechnicianSuggestion);

        Assertions.assertFalse(order.getTechnicianSuggestions().isEmpty());
        Assertions.assertTrue(secondOrder.getTechnicianSuggestions().isEmpty());

    }

    @Test
    @org.junit.jupiter.api.Order(16)
    @Transactional
    public void technicianCanNotSendSuggestionToOrdersWhichAreRelatedToHimButAreNotInProperState(){

        technicianCanSeeListOfOrdersRelatedToHim();

        order.setOrderStatus(OrderStatus.STARTED);
        orderService.saveOrUpdate(order);

        technicianSuggestionService.saveOrUpdate(technicianSuggestion);
        secondTechnicianSuggestion.setTechnician(technician);
        secondTechnicianSuggestion.setOrder(secondOrder);
        technicianSuggestionService.saveOrUpdate(secondTechnicianSuggestion);

        technicianService.sendTechnicianSuggestion(technician.getUsername(),order.getId(),technicianSuggestion);
        technicianService.sendTechnicianSuggestion(technician.getUsername(),secondOrder.getId(),secondTechnicianSuggestion);

        Assertions.assertTrue(order.getTechnicianSuggestions().isEmpty());
        Assertions.assertTrue(secondOrder.getTechnicianSuggestions().isEmpty());

    }
}