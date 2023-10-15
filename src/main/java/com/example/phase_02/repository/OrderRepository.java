package com.example.phase_02.repository;

import com.example.phase_02.entity.Customer;
import com.example.phase_02.entity.Order;
import com.example.phase_02.entity.Technician;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Optional<List<Order>> findRelatedOrders(Technician technician);
    Optional<List<Order>> findByCustomer(Customer customer);
}
