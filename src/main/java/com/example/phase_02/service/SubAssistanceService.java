package com.example.phase_02.service;

import com.example.phase_02.basics.baseService.BaseService;
import com.example.phase_02.entity.SubAssistance;
import com.example.phase_02.entity.Assistance;

import java.util.List;

public interface SubAssistanceService extends BaseService<SubAssistance> {

    SubAssistance findSubAssistance(String title, Assistance assistance);
}
