package com.example.phase_02.repository.jpaRepository;

import com.example.phase_02.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerJpaRepository extends JpaRepository<Customer,Long> {
}
