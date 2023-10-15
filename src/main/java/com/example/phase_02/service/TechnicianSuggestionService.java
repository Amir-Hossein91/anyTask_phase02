package com.example.phase_02.service;

import com.example.phase_02.basics.baseService.BaseService;
import com.example.phase_02.entity.Order;
import com.example.phase_02.entity.TechnicianSuggestion;
import com.example.phase_02.entity.dto.TechnicianSuggestionDTO;

import java.util.List;

public interface TechnicianSuggestionService extends BaseService<TechnicianSuggestion> {
    List<TechnicianSuggestionDTO> getSuggestionsOf(Order order);
}
