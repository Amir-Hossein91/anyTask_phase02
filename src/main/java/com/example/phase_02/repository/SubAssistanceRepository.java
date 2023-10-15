package com.example.phase_02.repository;

import com.example.phase_02.entity.Assistance;
import com.example.phase_02.entity.SubAssistance;

import java.util.Optional;

public interface SubAssistanceRepository {
    Optional<SubAssistance> findSubAssistance(String title, Assistance assistance);
}
