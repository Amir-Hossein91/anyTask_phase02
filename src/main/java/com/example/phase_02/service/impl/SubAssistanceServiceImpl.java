package com.example.phase_02.service.impl;

import com.example.phase_02.basics.baseService.impl.BaseServiceImpl;
import com.example.phase_02.entity.*;
import com.example.phase_02.exceptions.DeactivatedTechnicianException;
import com.example.phase_02.exceptions.NoSuchAsssistanceCategoryException;
import com.example.phase_02.exceptions.NotFoundException;
import com.example.phase_02.repository.SubAssistanceRepository;
import com.example.phase_02.service.SubAssistanceService;
import com.example.phase_02.utility.Constants;
import com.example.phase_02.exceptions.DuplicateSubAssistanceException;
import jakarta.persistence.PersistenceException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SubAssistanceServiceImpl extends BaseServiceImpl<SubAssistance> implements SubAssistanceService {

    private final SubAssistanceRepository repository;
    private final ManagerServiceImpl managerService;
    private final PersonServiceImple personService;
    private final AssistanceServiceImpl assistanceService;

    public SubAssistanceServiceImpl(SubAssistanceRepository repository,
                                    ManagerServiceImpl managerService,
                                    @Lazy PersonServiceImple personService,
                                    AssistanceServiceImpl assistanceService) {
        super();
        this.repository = repository;
        this.managerService = managerService;
        this.personService = personService;
        this.assistanceService = assistanceService;
    }

    @Override
    public SubAssistance saveOrUpdate(SubAssistance t) {
        if(!isValid(t))
            return null;
        try{
            return repository.save(t);
        } catch (RuntimeException e){
//            if(transaction.isActive())
//                transaction.rollback();
            printer.printError(e.getMessage());
            printer.printError(Arrays.toString(e.getStackTrace()));
            input.nextLine();
            return null;
        }
    }

    @Override
    public void delete(SubAssistance t) {
        if(!isValid(t))
            return;
        try{
            repository.delete(t);
        } catch (RuntimeException e){
            if(e instanceof PersistenceException)
                printer.printError("Could not delete " + repository.getClass().getSimpleName());
            else
                printer.printError("Could not complete deletion. Specified " + repository.getClass().getSimpleName() + " not found!");
            printer.printError(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public SubAssistance findById(long id) {
        try{
            return repository.findById(id).orElseThrow(()-> new NotFoundException("\nCould not find " + repository.getClass().getSimpleName()
                    + " with id = " + id));
        } catch (RuntimeException | NotFoundException e){
            printer.printError(e.getMessage());
            return null;
        }
    }

    @Override
    public List<SubAssistance> findAll() {
        try{
            return repository.findAll();
        } catch (RuntimeException e){
            printer.printError(e.getMessage());
            return null;
        }
    }

    @Override
    public SubAssistance findSubAssistance(String title, Assistance assistance) {
        return repository.findByTitleAndAssistance(title, assistance).orElse(null);
    }

    public SubAssistance specifySubAssistance(Assistance assistance, String title){
        printer.getInput("base price");
        long basePrice = input.nextLong();
        input.nextLine();
        printer.getInput("descriptions");
        String description = input.nextLine();
        return SubAssistance.builder().assistance(assistance).title(title).basePrice(basePrice).about(description).build();
    }

    public void addSubAssistance(String username, String assistanceTitle, String subAssistanceTitle){
        Manager manager = managerService.findByUsername(username);
        if(manager != null){
            try{
                Assistance assistance = assistanceService.findAssistance(assistanceTitle);
                if(assistance == null)
                    throw new NoSuchAsssistanceCategoryException(Constants.NO_SUCH_ASSISTANCE_CATEGORY);
                if(findSubAssistance(subAssistanceTitle,assistance) != null)
                    throw new DuplicateSubAssistanceException(Constants.SUBASSISTANCE_ALREADY_EXISTS);
                saveOrUpdate(specifySubAssistance(assistance, subAssistanceTitle));
            } catch (DuplicateSubAssistanceException | NoSuchAsssistanceCategoryException e) {
                printer.printError(e.getMessage());
            }
        }
        else
            printer.printError(("Only manager can add sub-assistance titles"));
    }

    public List<String> showSubAssistances(String userName){
        Person person = personService.findByUsername(userName);
        if(person instanceof Manager){
            return findAll().stream().map(Object::toString).toList();
        }
        else {
            try{
                if(person instanceof Technician && !((Technician) person).isActive())
                    throw new DeactivatedTechnicianException(Constants.DEACTIVATED_TECHNICIAN);
                List<SubAssistance> subAssistanceList = findAll();
                Map<String,List<String>> result = new HashMap<>();
                for(SubAssistance s : subAssistanceList){
                    String assistance = s.getAssistance().getTitle();
                    String subAssistance = s.getTitle() + "--> base price = " + s.getBasePrice()
                            + ", description = " + s.getAbout();
                    if(result.containsKey(assistance)){
                        result.get(assistance).add(subAssistance);
                    }
                    else
                        result.put(assistance,new ArrayList<>(List.of(subAssistance)));
                }
                StringBuilder stringBuilder = new StringBuilder();

                for(Map.Entry<String,List<String>> m : result.entrySet()){
                    stringBuilder.append(m.getKey()).append(": \n");
                    for(String s : result.get(m.getKey())){
                        stringBuilder.append("\t*").append(s).append("\n");
                    }
                }
                return List.of(stringBuilder.toString());
            } catch (DeactivatedTechnicianException e){
                printer.printError(e.getMessage());
                return List.of();
            }

        }
    }

    public void changeDescription(String managerUsername,String assistanceTitle, String subAssistanceTitle, String newDescription){
        Manager manager = managerService.findByUsername(managerUsername);
        if(manager != null){
            try{
                Assistance assistance = assistanceService.findAssistance(assistanceTitle);
                if(assistance == null)
                    throw new NotFoundException(Constants.NO_SUCH_ASSISTANCE_CATEGORY);
                SubAssistance subAssistance = findSubAssistance(subAssistanceTitle,assistance);
                if(subAssistance== null)
                    throw new NotFoundException(Constants.NO_SUCH_SUBASSISTANCE);
                subAssistance.setAbout(newDescription);
                saveOrUpdate(subAssistance);
            } catch (NotFoundException e) {
                printer.printError(e.getMessage());
            }
        }
        else {
            printer.printError(("Only manager can change description of a sub-assistance"));
        }
    }

    public void changeBasePrice(String managerUsername,String assistanceTitle, String subAssistanceTitle, long basePrice){
        Manager manager = managerService.findByUsername(managerUsername);
        if(manager != null){
            try{
                Assistance assistance = assistanceService.findAssistance(assistanceTitle);
                if(assistance == null)
                    throw new NotFoundException(Constants.NO_SUCH_ASSISTANCE_CATEGORY);
                SubAssistance subAssistance = findSubAssistance(subAssistanceTitle,assistance);
                if(subAssistance== null)
                    throw new NotFoundException(Constants.NO_SUCH_SUBASSISTANCE);
                subAssistance.setBasePrice(basePrice);
                saveOrUpdate(subAssistance);
            } catch (NotFoundException e) {
                printer.printError(e.getMessage());
            }
        }
        else {
            printer.printError(("Only manager can change description of a sub-assistance"));
        }
    }
}
