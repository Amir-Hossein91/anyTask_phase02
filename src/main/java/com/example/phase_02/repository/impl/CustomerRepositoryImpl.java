package com.example.phase_02.repository.impl;

import com.example.phase_02.basics.baseRepository.impl.BaseRepositoryImpl;
import com.example.phase_02.entity.Customer;

public class CustomerRepositoryImpl extends BaseRepositoryImpl<Customer> {

    public CustomerRepositoryImpl(Class<Customer> className) {
        super(className);
    }
}
