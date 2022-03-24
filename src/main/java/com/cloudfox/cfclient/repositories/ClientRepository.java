package com.cloudfox.cfclient.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudfox.cfclient.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
