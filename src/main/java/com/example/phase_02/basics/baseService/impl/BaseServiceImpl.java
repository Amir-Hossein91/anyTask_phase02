package com.example.phase_02.basics.baseService.impl;

import com.example.phase_02.basics.baseRepository.impl.BaseRepositoryImpl;
import com.example.phase_02.basics.baseService.BaseService;
import com.example.phase_02.connection.Connection;
import com.example.phase_02.entity.base.BaseEntity;
import com.example.phase_02.exceptions.NotFoundException;
import com.example.phase_02.exceptions.NotSavedException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import com.example.phase_02.utility.ApplicationContext;
import com.example.phase_02.utility.Printer;
import com.example.phase_02.validation.EntityValidator;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class BaseServiceImpl<R extends BaseRepositoryImpl<T>, T extends BaseEntity> implements BaseService<T> {
    protected R repository;
    protected EntityTransaction transaction;
    protected Validator validator;
    protected Printer printer;
    protected Scanner input;

    public BaseServiceImpl(R repository){
        this.repository = repository;
        transaction = Connection.entityManager.getTransaction();
        validator = EntityValidator.validator;
        printer = ApplicationContext.printer;
        input = ApplicationContext.input;
    }

    @Override
    public T saveOrUpdate(T t) {
        try{
            if(!isValid(t))
                return null;
            if(!transaction.isActive()){
                transaction.begin();
                t = repository.saveOrUpdate(t).orElseThrow(()-> new NotSavedException("\nCould not save " + repository.getClassName().getSimpleName()));
                transaction.commit();
            }
            else
                t = repository.saveOrUpdate(t).orElseThrow(()-> new NotSavedException("\nCould not save " + repository.getClassName().getSimpleName()));
            if(t != null)
                printer.printMessage(repository.getClassName().getSimpleName()  + " saved successfully!");
            return t;
        } catch (NotSavedException | RuntimeException e){
            if(transaction.isActive())
                transaction.rollback();
            printer.printError(e.getMessage());
            printer.printError(Arrays.toString(e.getStackTrace()));
            input.nextLine();
            return null;
        }
    }

    @Override
    public void delete(T t) {
        try{
            if(!isValid(t))
                return;
            if(!transaction.isActive()){
                transaction.begin();
                repository.delete(t);
                transaction.commit();
            }
            else
                repository.delete(t);
        } catch (RuntimeException e){
            if(transaction.isActive())
                transaction.rollback();
            if(e instanceof PersistenceException)
                printer.printError("Could not delete " + repository.getClassName().getSimpleName());
            else
                printer.printError("Could not complete deletion. Specified " + repository.getClassName().getSimpleName() + " not found!");
            printer.printError(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public T findById(long id) {
        try{
            return repository.findById(id).orElseThrow(()-> new NotFoundException("\nCould not find " + repository.getClassName().getSimpleName()
                    + " with id = " + id));
        } catch (RuntimeException | NotFoundException e){
            printer.printError(e.getMessage());
            return null;
        }
    }

    @Override
    public List<T> findAll() {
        try{
            return repository.findAll().orElseThrow(()-> new NotFoundException("\nThere is no " + repository.getClassName().getSimpleName()
                    + " registered!"));
        }catch (RuntimeException | NotFoundException e){
            printer.printError(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isValid(T t) {
        Validator validator = EntityValidator.validator;
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if(!violations.isEmpty()){
            for(ConstraintViolation<T> c : violations)
                printer.printError(c.getMessage());
            return false;
        }
        return true;
    }
}
