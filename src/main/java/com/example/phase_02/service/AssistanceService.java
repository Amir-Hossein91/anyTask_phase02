package com.example.phase_02.service;

import com.example.phase_02.basics.baseService.BaseService;
import com.example.phase_02.entity.Assistance;


public interface AssistanceService extends BaseService<Assistance> {

    Assistance findAssistance(String assistanceName);
}
