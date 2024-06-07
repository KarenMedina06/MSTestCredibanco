package com.credibanco.mstest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.credibanco.mstest.entities.Transaction;


@Component
public interface ITransactionRepository extends JpaRepository<Transaction, Long>{

}
