package com.example.phase_02.utility;

import com.example.phase_02.entity.*;
import com.example.phase_02.repository.impl.*;
import com.example.phase_02.service.impl.*;
import com.github.mfathi91.time.PersianDate;
import entity.*;
import repository.impl.*;
import service.impl.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Scanner;

public class ApplicationContext {
    public static final Path outputPath;
    public static final Path inputPath;
    public static final String sourceAddress;
    public static final String imageName;
    public static final String imageExtension;
    public static final PersianDate currentPersianDate;
    public static final LocalDate currentDate;
    public static final Printer printer;
    public static final Scanner input;
    private static final AssistanceRepositoryImpl assistanceRepository;
    private static final CustomerRepositoryImpl customerRepository;
    private static final ManagerRepositoryImpl managerRepository;
    private static final OrderRepositoryImpl orderRepository;
    private static final PersonRepositoryImpl personRepository;
    private static final SubAssistanceRepositoryImpl subAssistanceRepository;
    private static final OrderDescriptionRepositoryImpl orderDescriptionRepository;
    private static final TechnicianSuggestionRepositoryImpl technicianSuggestionRepository;
    public static final TechnicianRepositoryImpl technicianRepository;
    public static final AssistanceServiceImpl assistanceService;
    public static final CustomerServiceImpl customerService;
    public static final ManagerServiceImpl managerService;
    public static final OrderServiceImpl orderService;
    public static final PersonServiceImple personService;
    public static final SubAssistanceServiceImpl subAssistanceService;
    public static final TechnicianServiceImpl technicianService;
    public static final OrderDescriptionServiceImpl orderDescriptionSerice;
    public static final TechnicianSuggestionServiceImpl technicianSuggestionService;

    static{
        sourceAddress = "image_input";
        imageName = "technician_01";
        imageExtension = "js";
        inputPath = Paths.get(sourceAddress,imageName+"."+imageExtension);
        outputPath = Paths.get("image_output/" + imageName + ".jpg");
        currentPersianDate = PersianDate.now();
        currentDate = currentPersianDate.toGregorian();
        printer = new Printer();
        input = new Scanner(System.in);
        assistanceRepository = new AssistanceRepositoryImpl(Assistance.class);
        assistanceService = new AssistanceServiceImpl(assistanceRepository);
        customerRepository = new CustomerRepositoryImpl(Customer.class);
        customerService = new CustomerServiceImpl(customerRepository);
        managerRepository = new ManagerRepositoryImpl(Manager.class);
        managerService = new ManagerServiceImpl(managerRepository);
        orderRepository = new OrderRepositoryImpl(Order.class);
        orderService = new OrderServiceImpl(orderRepository);
        personRepository = new PersonRepositoryImpl(Person.class);
        personService = new PersonServiceImple(personRepository);
        subAssistanceRepository = new SubAssistanceRepositoryImpl(SubAssistance.class);
        subAssistanceService = new SubAssistanceServiceImpl(subAssistanceRepository);
        technicianRepository = new TechnicianRepositoryImpl(Technician.class);
        technicianService = new TechnicianServiceImpl(technicianRepository);
        orderDescriptionRepository = new OrderDescriptionRepositoryImpl(OrderDescription.class);
        orderDescriptionSerice = new OrderDescriptionServiceImpl(orderDescriptionRepository);
        technicianSuggestionRepository = new TechnicianSuggestionRepositoryImpl(TechnicianSuggestion.class);
        technicianSuggestionService = new TechnicianSuggestionServiceImpl(technicianSuggestionRepository);
    }
}
