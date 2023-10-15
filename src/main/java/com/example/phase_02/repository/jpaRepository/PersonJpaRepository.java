package com.example.phase_02.repository.jpaRepository;

import com.example.phase_02.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonJpaRepository extends JpaRepository<Person,Long> {
    Optional<Person> findByUsername (String username);
}
