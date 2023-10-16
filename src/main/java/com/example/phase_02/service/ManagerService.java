package com.example.phase_02.service;

import com.example.phase_02.basics.baseService.BaseService;
import com.example.phase_02.entity.Manager;


public interface ManagerService extends BaseService<Manager> {

    boolean doesManagerExist();
    Manager findByUsername(String managerUsername);
}
