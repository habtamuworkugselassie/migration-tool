package com.client.migration.repository;

import com.client.migration.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author habtamugebreselassie
 * Date: 06/01/2026
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByMigratedFalse();
    List<Client> findByMigratedTrue();
}


