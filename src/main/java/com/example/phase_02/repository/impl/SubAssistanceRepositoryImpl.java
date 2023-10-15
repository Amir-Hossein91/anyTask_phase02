package com.example.phase_02.repository.impl;

import com.example.phase_02.basics.baseRepository.impl.BaseRepositoryImpl;
import com.example.phase_02.entity.Assistance;
import com.example.phase_02.entity.SubAssistance;
import com.example.phase_02.repository.SubAssistanceRepository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Optional;

public class SubAssistanceRepositoryImpl extends BaseRepositoryImpl<SubAssistance> implements SubAssistanceRepository {

    public SubAssistanceRepositoryImpl(Class<SubAssistance> className) {
        super(className);
    }

    @Override
    public Optional<SubAssistance> findSubAssistance(String title, Assistance assistance) {
        String queryLine = "from SubAssistance where title=:t and assistance=:a";
        Query query = entityManager.createQuery(queryLine);
        query.setParameter("t",title);
        query.setParameter("a",assistance);
        try {
            return Optional.of((SubAssistance) query.getSingleResult());
        } catch (NoResultException e){
            return Optional.empty();
        }
    }
}
