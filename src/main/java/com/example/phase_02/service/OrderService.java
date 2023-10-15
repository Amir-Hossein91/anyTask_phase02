package com.example.phase_02.service;

import com.example.phase_02.entity.Order;
import com.example.phase_02.entity.dto.OrderDTO;
import com.example.phase_02.entity.Customer;
import com.example.phase_02.entity.Technician;

import java.util.List;

public interface OrderService {
    List<OrderDTO> findRelatedOrders(Technician technician);
    void sendTechnicianSuggestion(Technician technician, Order order);
    List<Order> findByCustomer(Customer customer);
}
