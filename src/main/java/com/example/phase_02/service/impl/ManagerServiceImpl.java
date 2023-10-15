package com.example.phase_02.service.impl;

import com.example.phase_02.basics.baseService.impl.BaseServiceImpl;
import com.example.phase_02.entity.Manager;
import com.example.phase_02.exceptions.NotFoundException;
import com.example.phase_02.repository.ManagerRepository;
import com.example.phase_02.service.ManagerService;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class ManagerServiceImpl extends BaseServiceImpl<Manager> implements ManagerService {

    private final ManagerRepository repository;
    public ManagerServiceImpl(ManagerRepository repository) {
        super();
        this.repository = repository;
    }

    public Manager specifyManager(){
        if(!doesManagerExist()) {
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
            return Manager.builder().firstName(firstname).lastName(lastname).email(email).username(username)
                    .password(password).registrationDate(registrationDate).build();
        }
        return null;
    }

    @Override
    public Manager saveOrUpdate(Manager t) {
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
    public void delete(Manager t) {
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
    public Manager findById(long id) {
        try{
            return repository.findById(id).orElseThrow(()-> new NotFoundException("\nCould not find " + repository.getClass().getSimpleName()
                    + " with id = " + id));
        } catch (RuntimeException | NotFoundException e){
            printer.printError(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Manager> findAll() {
        try{
            return repository.findAll();
        } catch (RuntimeException e){
            printer.printError(e.getMessage());
            return null;
        }
    }

    public boolean doesManagerExist(){
        return !repository.findAll().isEmpty();
    }
}
