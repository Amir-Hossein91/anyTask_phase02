package com.example.phase_02.repository.impl;

import com.example.phase_02.basics.baseRepository.impl.BaseRepositoryImpl;
import com.example.phase_02.entity.Assistance;
import com.example.phase_02.repository.AssistanceRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.util.Optional;

public class AssistanceRepositoryImpl extends BaseRepositoryImpl<Assistance> implements AssistanceRepository {

    public AssistanceRepositoryImpl(Class<Assistance> className) {
        super(className);
    }

    @Override
    public Optional<Assistance> findAssistance(String assistanceName) {
        String queryLine = "from Assistance where title=:t";
        Query query = entityManager.createQuery(queryLine);
        query.setParameter("t",assistanceName);
        try {
            return Optional.of((Assistance) query.getSingleResult());
        } catch (NoResultException e){
            return Optional.empty();
        }
    }
}
