package com.example.phase_02.service;

import com.example.phase_02.entity.SubAssistance;
import com.example.phase_02.entity.Assistance;

public interface SubAssistanceService {
    SubAssistance findSubAssistance(String title, Assistance assistance);
}
