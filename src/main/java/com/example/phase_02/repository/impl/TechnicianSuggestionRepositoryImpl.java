package com.example.phase_02.repository.impl;

import com.example.phase_02.basics.baseRepository.impl.BaseRepositoryImpl;
import com.example.phase_02.entity.Order;
import com.example.phase_02.entity.TechnicianSuggestion;
import com.example.phase_02.repository.TechnicianSuggestionRepository;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class TechnicianSuggestionRepositoryImpl extends BaseRepositoryImpl<TechnicianSuggestion>
implements TechnicianSuggestionRepository {

    public TechnicianSuggestionRepositoryImpl(Class<TechnicianSuggestion> className) {
        super(className);
    }

    @Override
    public Optional<List<TechnicianSuggestion>> findTechnitionSugestions(Order order) {
        String queryLine = "from TechnicianSuggestion where order =:o";
        Query query = entityManager.createQuery(queryLine);
        query.setParameter("o",order);
        return Optional.of((List<TechnicianSuggestion>)query.getResultList());

    }
}
