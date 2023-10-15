package com.example.phase_02.service;

import com.example.phase_02.entity.Person;

public interface PersonService {
    Person findByUsername(String username);
}
