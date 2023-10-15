package com.example.phase_02.repository;

import com.example.phase_02.entity.Person;

import java.util.Optional;

public interface PersonRepository {
    Optional<Person> findByUsername(String username);
}
