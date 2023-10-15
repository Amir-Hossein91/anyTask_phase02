package com.example.phase_02.repository;

import com.example.phase_02.entity.Order;
import com.example.phase_02.entity.TechnicianSuggestion;

import java.util.List;
import java.util.Optional;

public interface TechnicianSuggestionRepository {
    Optional<List<TechnicianSuggestion>> findTechnitionSugestions(Order order);
}
