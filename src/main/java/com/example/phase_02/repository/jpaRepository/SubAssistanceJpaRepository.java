package com.example.phase_02.repository.jpaRepository;

import com.example.phase_02.entity.Assistance;
import com.example.phase_02.entity.SubAssistance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubAssistanceJpaRepository extends JpaRepository<SubAssistance,Long> {
    Optional<SubAssistance> findByTitleAndAssistance (String title, Assistance assistance);
}
