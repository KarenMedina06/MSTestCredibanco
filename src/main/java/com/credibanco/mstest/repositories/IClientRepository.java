package com.credibanco.mstest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.credibanco.mstest.entities.Clients;

public interface IClientRepository extends JpaRepository<Clients, Long>{
	
}
