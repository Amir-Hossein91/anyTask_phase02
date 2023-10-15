package com.example.phase_02.service.impl;

import com.example.phase_02.basics.baseService.impl.BaseServiceImpl;
import com.example.phase_02.entity.Assistance;
import com.example.phase_02.entity.Manager;
import com.example.phase_02.entity.Person;
import com.example.phase_02.entity.Technician;
import com.example.phase_02.exceptions.DeactivatedTechnicianException;
import com.example.phase_02.exceptions.DuplicateAssistanceException;
import com.example.phase_02.exceptions.NotFoundException;
import com.example.phase_02.repository.impl.AssistanceRepositoryImpl;
import com.example.phase_02.repository.impl.PersonRepositoryImpl;
import com.example.phase_02.service.AssistanceService;
import com.example.phase_02.utility.Constants;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssistanceServiceImpl extends BaseServiceImpl<AssistanceRepositoryImpl, Assistance> implements AssistanceService {

    private PersonServiceImple personService;

    public AssistanceServiceImpl(AssistanceRepositoryImpl repository) {
        super(repository);
        PersonRepositoryImpl personRepository = new PersonRepositoryImpl(Person.class);
        personService = new PersonServiceImple(personRepository);
//        personService = ApplicationContext.personService;
    }

    @Override
    public Assistance findAssistance(String assistanceName) {
        return repository.findAssistance(assistanceName).orElse(null);
    }

    public void addAssistance(String username, String assistanceName){
        Person person = personService.findByUsername(username);
        if(person instanceof Manager){
            try {
                if (findAssistance(assistanceName) != null)
                    throw new DuplicateAssistanceException(Constants.ASSISTANCE_ALREADY_EXISTS);
                Assistance assistance = Assistance.builder().title(assistanceName).build();
                saveOrUpdate(assistance);
            } catch (DuplicateAssistanceException e ){
                printer.printError(e.getMessage());
            }
        }
        else
            printer.printError("Only manager can add assistance categories");
    }

    public List<String> seeAssistances(String personUsername){
        Person person = personService.findByUsername(personUsername);
        try {
            if(person == null)
                throw new NotFoundException(Constants.INVALID_USERNAME);
            if(person instanceof Technician && !((Technician) person).isActive())
                throw new DeactivatedTechnicianException(Constants.DEACTIVATED_TECHNICIAN);
            return findAll().stream().map(Object::toString).toList();
        } catch (NotFoundException | DeactivatedTechnicianException e) {
            printer.printError(e.getMessage());
            return List.of();
        }
    }
}
