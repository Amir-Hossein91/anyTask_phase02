package com.example.phase_02.service.impl;

import com.example.phase_02.basics.baseService.impl.BaseServiceImpl;
import com.example.phase_02.entity.*;
import com.example.phase_02.entity.enums.OrderStatus;
import com.example.phase_02.exceptions.NotFoundException;
import com.example.phase_02.repository.impl.CustomerRepositoryImpl;
import com.example.phase_02.repository.impl.OrderRepositoryImpl;
import com.example.phase_02.repository.impl.PersonRepositoryImpl;
import com.example.phase_02.utility.Constants;
import entity.*;
import com.example.phase_02.entity.dto.TechnicianSuggestionDTO;
import com.example.phase_02.exceptions.NotEnoughCreditException;
import com.example.phase_02.repository.impl.TechnicianSuggestionRepositoryImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerServiceImpl extends BaseServiceImpl<CustomerRepositoryImpl, Customer> {

    private final PersonServiceImple personService;
    private final OrderServiceImpl orderService;
    private final TechnicianSuggestionServiceImpl technicianSuggestionService;

    public CustomerServiceImpl(CustomerRepositoryImpl repository) {
        super(repository);
        OrderRepositoryImpl orderRepository = new OrderRepositoryImpl(Order.class);
        orderService = new OrderServiceImpl(orderRepository);
//        orderService = ApplicationContext.orderService;
        PersonRepositoryImpl personRepository = new PersonRepositoryImpl(Person.class);
        personService = new PersonServiceImple(personRepository);
//        personService = ApplicationContext.personService;
        TechnicianSuggestionRepositoryImpl technicianSuggestionRepository = new TechnicianSuggestionRepositoryImpl(TechnicianSuggestion.class);
        technicianSuggestionService = new TechnicianSuggestionServiceImpl(technicianSuggestionRepository);
//        technicianSuggestionService = ApplicationContext.technicianSuggestionService;
    }

    public Customer specifyCustomer(){
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
        printer.getInput("initial credit");
        long credit = input.nextLong();
        input.nextLine();
        return Customer.builder().firstName(firstname).lastName(lastname).email(email).username(username)
                .password(password).registrationDate(registrationDate).credit(credit).build();
    }

    public List<String> showAllCustomers(String managerUsername){
        Person person = personService.findByUsername(managerUsername);
        if(person instanceof Manager){
            return findAll().stream().map(Object::toString).toList();
        }
        else{
            printer.printError("Only manager can see the list of all customers");
            return List.of();
        }
    }

    public List<String> seeOrdersOf (String customerUsername){
        Person person = personService.findByUsername(customerUsername);
        if(person instanceof Customer customer){
            return orderService.findByCustomer(customer).stream().map(Object::toString).toList();
        }
        else {
            printer.printError("this function is only available for 'customers'");
            return List.of();
        }
    }

    private boolean isSuggestionChoosingPossible(Person person, Order order){
        try{
            if(order == null)
                throw new NotFoundException(Constants.NO_SUCH_ORDER);

            if(!order.getCustomer().equals(person))
                throw new NotFoundException(Constants.ORDER_NOT_BELONG_TO_CUSTOMER);

            if(!(order.getOrderStatus() == OrderStatus.WAITING_FOR_TECHNICIANS_SUGGESTIONS
                    || order.getOrderStatus() == OrderStatus.CHOOSING_TECHNICIAN))
                throw new NotFoundException(Constants.SUGGESTION_NOT_AVAILABLE_IN_THIS_STATUS);

        } catch (NotFoundException e) {
            printer.printError(e.getMessage());
            return false;
        }
        return true;
    }

    public List<String> seeTechnicianSuggestions(String customerUsername, long orderId){
        Person person = personService.findByUsername(customerUsername);
        if(person instanceof Customer){
            Order order = orderService.findById(orderId);
            if(!isSuggestionChoosingPossible(person,order))
                return List.of();

            List<TechnicianSuggestionDTO> technicianSuggestions = technicianSuggestionService.getSuggestionsOf(order);
            if(technicianSuggestions == null)
                return List.of();

            if(order.getOrderStatus() == OrderStatus.WAITING_FOR_TECHNICIANS_SUGGESTIONS) {
                order.setOrderStatus(OrderStatus.CHOOSING_TECHNICIAN);
                orderService.saveOrUpdate(order);
            }
            return technicianSuggestions.stream().map(Object::toString).toList();
        }
        else {
            printer.printError("Only customers have access to this function");
            return List.of();
        }
    }

    public void chooseSuggestion(String customerUsername, long orderId, long suggestionId){
        Person person = personService.findByUsername(customerUsername);
        if(person instanceof Customer){
            Order order = orderService.findById(orderId);
            if(!isSuggestionChoosingPossible(person,order))
                return;

            List<TechnicianSuggestionDTO> technicianSuggestions = technicianSuggestionService.getSuggestionsOf(order);
            try{
                if(technicianSuggestions == null)
                    throw new NotFoundException(Constants.NO_TECHNICIAN_SUGGESTION_FOUND);

                List<Long> suggestionsIds = technicianSuggestions.stream()
                        .map(TechnicianSuggestionDTO::getSuggestionId)
                        .toList();

                TechnicianSuggestion suggestion = technicianSuggestionService.findById(suggestionId);
                if(suggestion == null)
                    throw new NotFoundException(Constants.TECHNICIAN_SUGGESTION_NOT_EXIST);

                if(!suggestionsIds.contains(suggestion.getId()))
                    throw new NotFoundException(Constants.TECHNICIAN_SUGGESTION_NOT_IN_LIST);

                order.setTechnician(suggestion.getTechnician());
                order.setOrderStatus(OrderStatus.STARTED);
                order = orderService.saveOrUpdate(order);

                if(order != null)
                    printer.printMessage("Technician successfully assigned to the order");
            } catch (NotFoundException e) {
                printer.printError(e.getMessage());
            }
        }
        else
            printer.printError("Only customers have access to this function");
    }

    public void payThePrice(String customerUsername, long orderId){
        Person person = personService.findByUsername(customerUsername);
        if(person instanceof Customer customer){
            try{
                Order order = orderService.findById(orderId);
                if(order == null)
                    throw new NotFoundException(Constants.NO_SUCH_ORDER);

                if(!order.getCustomer().equals(customer))
                    throw new NotFoundException(Constants.ORDER_NOT_BELONG_TO_CUSTOMER);

                if(order.getOrderStatus() != OrderStatus.STARTED)
                    throw new IllegalStateException(Constants.PAYING_NOT_POSSIBLE_IN_THIS_STATE);

                TechnicianSuggestion selecteSuggestion = new TechnicianSuggestion();
                Technician selectedTechnician = order.getTechnician();
                for(TechnicianSuggestion t : order.getTechnicianSuggestions()){
                    Technician test = t.getTechnician();
                    if(test == selectedTechnician){
                        selecteSuggestion = t;
                        break;
                    }
                }
                customer.setCredit(customer.getCredit() - selecteSuggestion.getTechSuggestedPrice());
                if(customer.getCredit() < 0)
                    throw new NotEnoughCreditException(Constants.NOT_ENOUGH_CREDIT);

                selectedTechnician.setCredit(selectedTechnician.getCredit() + selecteSuggestion.getTechSuggestedPrice());
                selectedTechnician.setNumberOfFinishedTasks(selectedTechnician.getNumberOfFinishedTasks() + 1);
                order.setOrderStatus(OrderStatus.FINISHED);
                customer = saveOrUpdate(customer);
                order = orderService.saveOrUpdate(order);

                if(customer != null && order != null)
                    printer.printMessage("Payment successful");

            } catch (IllegalStateException | NotFoundException | NotEnoughCreditException e) {
                printer.printError(e.getMessage());
            }
        }
        else
            printer.printError("Paying the price is an act of 'customer'");
    }

    public void scoreTheTechnician(String customerUsername, long orderId){
        Person person = personService.findByUsername(customerUsername);
        if(person instanceof Customer customer){
            try{
                Order order = orderService.findById(orderId);
                if(order == null)
                    throw new NotFoundException(Constants.NO_SUCH_ORDER);

                if(!order.getCustomer().equals(customer))
                    throw new NotFoundException(Constants.ORDER_NOT_BELONG_TO_CUSTOMER);

                if(order.getOrderStatus() != OrderStatus.FINISHED)
                    throw new IllegalStateException(Constants.SCORING_NOT_POSSIBLE_IN_THIS_STATE);

                Technician selectedTechnician = order.getTechnician();

                printer.getInput("Technician score (1-5)");
                int score = input.nextInt();
                input.nextLine();
                printer.getInput("Your opinion");
                String opinion = input.nextLine();

                selectedTechnician.setScore(selectedTechnician.getScore() + score);
                order.setTechnicianScore(score);
                order.setTechEvaluation(opinion);
                order = orderService.saveOrUpdate(order);

                if(order != null)
                    printer.printMessage("Scoring successful");

            } catch (IllegalStateException | NotFoundException e) {
                printer.printError(e.getMessage());
            }
        }
        else
            printer.printError("Scoring the 'technician' is an act of 'customer'");
    }
}
