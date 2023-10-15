package com.example.phase_02.service.impl;

import com.example.phase_02.basics.baseService.impl.BaseServiceImpl;
import com.example.phase_02.repository.PersonRepository;
import com.example.phase_02.utility.ApplicationContext;
import com.example.phase_02.utility.Constants;
import com.example.phase_02.entity.Manager;
import com.example.phase_02.entity.Person;
import com.example.phase_02.entity.Technician;
import com.example.phase_02.exceptions.NotFoundException;

import com.example.phase_02.service.PersonService;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class PersonServiceImple extends BaseServiceImpl<Person> implements PersonService {

    private final PersonRepository repository;
    private final ManagerServiceImpl managerService;
    private final CustomerServiceImpl customerService;
    private final TechnicianServiceImpl technicianService;
    private final AssistanceServiceImpl assistanceService;
    private final SubAssistanceServiceImpl subAssistanceService;

    public PersonServiceImple(PersonRepository repository,
                              ManagerServiceImpl managerService,
                              CustomerServiceImpl customerService,
                              TechnicianServiceImpl technicianService,
                              AssistanceServiceImpl assistanceService,
                              SubAssistanceServiceImpl subAssistanceService) {
        super();
        this.repository = repository;
        this.managerService = managerService;
        this.customerService = customerService;
        this.technicianService = technicianService;
        this.assistanceService = assistanceService;
        this.subAssistanceService = subAssistanceService;
    }

    public Person specifyPerson(){
        printer.getInput("first name");
        String firstname = input.nextLine();
        printer.getInput("last name");
        String lastname = input.nextLine();
        printer.getInput("email");
        String email = input.nextLine();
        printer.getInput("user name");
        String username = input.nextLine();
        printer.getInput("password");
        String password = input.nextLine();
        LocalDateTime registrationDate = LocalDateTime.now();
        return Person.builder().firstName(firstname).lastName(lastname).email(email).username(username)
                .password(password).registrationDate(registrationDate).build();
    }

    public void changePassword(String username, String oldPassword, String newPassword){
        Person fetched = findByUsername(username);
        if(fetched != null) {
            try {
                if (!fetched.getPassword().equals(oldPassword))
                    throw new IllegalArgumentException(Constants.INCORRECT_PASSWORD);
                fetched.setPassword(newPassword);
                saveOrUpdate(fetched);
                printer.printMessage("password changed successfully");
            } catch (IllegalArgumentException e) {
                printer.printError(e.getMessage());
            }
        }
    }

    @Override
    public Person saveOrUpdate(Person t) {
        if(!isValid(t))
            return null;
        try{
            return repository.save(t);
        } catch (RuntimeException e){
            if(transaction.isActive())
                transaction.rollback();
            printer.printError(e.getMessage());
            printer.printError(Arrays.toString(e.getStackTrace()));
            input.nextLine();
            return null;
        }
    }

    @Override
    public void delete(Person t) {
        if(!isValid(t))
            return;
        try{
            repository.delete(t);
        } catch (RuntimeException e){
            if(transaction.isActive())
                transaction.rollback();
            if(e instanceof PersistenceException)
                printer.printError("Could not delete " + repository.getClass().getSimpleName());
            else
                printer.printError("Could not complete deletion. Specified " + repository.getClass().getSimpleName() + " not found!");
            printer.printError(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public Person findById(long id) {
        try{
            return repository.findById(id).orElseThrow(()-> new NotFoundException("\nCould not find " + repository.getClass().getSimpleName()
                    + " with id = " + id));
        } catch (RuntimeException | NotFoundException e){
            printer.printError(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Person> findAll() {
        try{
            return repository.findAll();
        } catch (RuntimeException e){
            printer.printError(e.getMessage());
            return null;
        }
    }

    @Override
    public Person findByUsername(String username) {
        try {
            return repository.findByUsername(username).orElseThrow(()->new NotFoundException(Constants.INVALID_USERNAME));
        } catch (RuntimeException | NotFoundException e) {
            printer.printError(e.getMessage());
            return null;
        }
    }

    public Person register(){
        System.out.println("Rolls:");
        printer.printListWithSelect(List.of("Manager", "Technician", "Customer"));
        int choice = input.nextInt();
        input.nextLine();
        switch (choice){
            case 1 -> {
                Manager manager = managerService.specifyManager();
                if(manager == null){
                    printer.printError("This organization already has a defined manager");
                    return null;
                }
                return managerService.saveOrUpdate(manager);
            }
            case 2 -> {
                Path inputPath = ApplicationContext.inputPath;
                Path outputPath = ApplicationContext.outputPath;
                if(!technicianService.validateImage(inputPath))
                    return null;
                Technician technician = technicianService.specifyTechnician(inputPath);
                if(technician == null)
                    return null;
                Technician savedTechnician = technicianService.saveOrUpdate(technician);
                technicianService.saveImageToDirectory(outputPath,savedTechnician.getImage());
                return savedTechnician;
            }
            case 3 -> {
                return customerService.saveOrUpdate(customerService.specifyCustomer());
            }
            default -> {
                return saveOrUpdate(specifyPerson());
            }
        }
    }

    public void login(String username, String password){
        Person fetched = findByUsername(username);
        if(fetched != null){
            try {
                if (!fetched.getPassword().equals(password))
                    throw new IllegalArgumentException(Constants.INCORRECT_USERNAME_PASSWORD);
                printer.printMessage("Hello " + fetched.getFirstName() + ", you are a " + fetched.getClass().getSimpleName() + " here!");
            } catch (IllegalArgumentException e) {
                printer.printError(e.getMessage());
            }
        }
    }





}
