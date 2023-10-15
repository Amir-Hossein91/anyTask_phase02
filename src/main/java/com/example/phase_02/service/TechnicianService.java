package com.example.phase_02.service;

import com.example.phase_02.entity.Technician;

import java.util.List;

public interface TechnicianService {
    List<Technician> saveOrUpdate(List<Technician> technicians);
}
