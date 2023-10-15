package com.example.phase_02.basics.baseService.impl;

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
import org.springframework.stereotype.Service;
import java.util.Scanner;
import java.util.Set;

@Service
public class BaseServiceImpl<T extends BaseEntity> {

    protected EntityTransaction transaction;
    protected Validator validator;
    protected Printer printer;
    protected Scanner input;

    public BaseServiceImpl(){
        transaction = Connection.entityManager.getTransaction();
        validator = EntityValidator.validator;
        printer = ApplicationContext.printer;
        input = ApplicationContext.input;
    }

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
