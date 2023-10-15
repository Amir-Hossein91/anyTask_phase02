package com.example.phase_02.repository.jpaRepository;

import com.example.phase_02.entity.Order;
import com.example.phase_02.entity.TechnicianSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TechnicianSuggestionJpaRepository extends JpaRepository<TechnicianSuggestion,Long> {
    Optional<List<TechnicianSuggestion>> findByOrder (Order order);
}
