package com.example.phase_02.service.impl;

import com.example.phase_02.basics.baseService.impl.BaseServiceImpl;
import com.example.phase_02.entity.Manager;
import com.example.phase_02.repository.impl.ManagerRepositoryImpl;
import com.example.phase_02.service.ManagerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ManagerServiceImpl extends BaseServiceImpl<ManagerRepositoryImpl, Manager> implements ManagerService {

    public ManagerServiceImpl(ManagerRepositoryImpl repository) {
        super(repository);
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

    public boolean doesManagerExist(){
        return repository.doesManagerExist();
    }
}
