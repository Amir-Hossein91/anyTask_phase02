package com.example.phase_02.repository.jpaRepository;

import com.example.phase_02.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerJpaRepository extends JpaRepository<Manager,Long> {
}
