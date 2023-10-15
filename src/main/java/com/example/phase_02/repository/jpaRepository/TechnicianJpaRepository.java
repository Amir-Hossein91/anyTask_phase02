package com.example.phase_02.repository.jpaRepository;

import com.example.phase_02.entity.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TechnicianJpaRepository extends JpaRepository<Technician,Long> {
    @Query(value = "from Technician where technicianStatus <> 'APPROVED'")
    Optional<List<Technician>> findUnapproved();

    @Query(value = "from Technician where technicianStatus = 'APPROVED' and isActive = false")
    Optional<List<Technician>> findDeactivated();
}
