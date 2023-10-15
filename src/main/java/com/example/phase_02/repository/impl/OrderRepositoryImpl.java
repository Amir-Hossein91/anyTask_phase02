package com.example.phase_02.repository.impl;

import com.example.phase_02.basics.baseRepository.impl.BaseRepositoryImpl;
import com.example.phase_02.entity.Customer;
import com.example.phase_02.entity.Order;
import com.example.phase_02.entity.Technician;
import com.example.phase_02.repository.OrderRepository;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class OrderRepositoryImpl extends BaseRepositoryImpl<Order> implements OrderRepository {

    public OrderRepositoryImpl(Class<Order> className) {
        super(className);
    }

    public Optional<List<Order>> findRelatedOrders(Technician technician){
        String queryLine = """
                                select o from Technician t 
                                join t.subAssistances s 
                                join s.orders o 
                                where t=:tech 
                                and s = o.subAssistance
                                and (o.orderStatus = 'WAITING_FOR_TECHNICIANS_SUGGESTIONS'
                                or o.orderStatus = 'CHOOSING_TECHNICIAN')
                                """;
        Query query = entityManager.createQuery(queryLine);
        query.setParameter("tech",technician);
        return Optional.of((List<Order>)query.getResultList());
    }

    @Override
    public Optional<List<Order>> findByCustomer(Customer customer) {
        String queryLine = "from Order o where o.customer =:c";
        Query query = entityManager.createQuery(queryLine);
        query.setParameter("c",customer);
        return Optional.of((List<Order>)query.getResultList());
    }
}
