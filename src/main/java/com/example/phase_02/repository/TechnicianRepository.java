package com.example.phase_02.repository;

import com.example.phase_02.exceptions.NotSavedException;
import com.example.phase_02.entity.Technician;

import java.util.List;
import java.util.Optional;

public interface TechnicianRepository {
    Optional<List<Technician>> saveOrUpdate(List<Technician> technicians) throws NotSavedException;
    Optional<List<Technician>> findUnapproved();
    Optional<List<Technician>> findDeactivated();
}
