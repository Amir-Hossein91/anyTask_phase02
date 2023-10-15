package com.example.phase_02.service;

import com.example.phase_02.basics.baseService.BaseService;
import com.example.phase_02.entity.Person;

import java.util.List;

public interface PersonService extends BaseService<Person> {

    Person findByUsername(String username);
}
