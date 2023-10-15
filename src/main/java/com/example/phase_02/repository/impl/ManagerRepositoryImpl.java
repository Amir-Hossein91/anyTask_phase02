package com.example.phase_02.repository.impl;

import com.example.phase_02.basics.baseRepository.impl.BaseRepositoryImpl;
import com.example.phase_02.entity.Manager;
import com.example.phase_02.repository.ManagerRepository;

import javax.persistence.Query;

public class ManagerRepositoryImpl extends BaseRepositoryImpl<Manager> implements ManagerRepository {

    public ManagerRepositoryImpl(Class<Manager> className) {
        super(className);
    }

    @Override
    public boolean doesManagerExist() {
        String queryLine = "from Manager";
        Query query = entityManager.createQuery(queryLine);
        return !query.getResultList().isEmpty();
    }
}
