package com.example.phase_02.repository.jpaRepository;

import com.example.phase_02.entity.OrderDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDescriptionJpaRepository extends JpaRepository<OrderDescription,Long> {
}
