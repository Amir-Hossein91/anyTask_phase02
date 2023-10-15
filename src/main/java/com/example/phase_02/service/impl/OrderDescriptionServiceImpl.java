package com.example.phase_02.service.impl;

import com.example.phase_02.basics.baseService.impl.BaseServiceImpl;
import com.example.phase_02.entity.OrderDescription;
import com.example.phase_02.repository.impl.OrderDescriptionRepositoryImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderDescriptionServiceImpl extends
        BaseServiceImpl<OrderDescriptionRepositoryImpl, OrderDescription> {

    public OrderDescriptionServiceImpl(OrderDescriptionRepositoryImpl repository) {
        super(repository);
    }

}
