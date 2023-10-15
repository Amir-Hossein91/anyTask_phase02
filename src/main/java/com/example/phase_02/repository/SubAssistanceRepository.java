package com.example.phase_02.repository;

import com.example.phase_02.entity.Assistance;
import com.example.phase_02.entity.SubAssistance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubAssistanceRepository extends JpaRepository<SubAssistance,Long> {
    Optional<SubAssistance> findByTitleAndAssistance (String title, Assistance assistance);
}
