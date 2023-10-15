package com.example.phase_02.connection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


public class Connection {
    private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("anyTask");
    public final static EntityManager entityManager = emf.createEntityManager();
}
