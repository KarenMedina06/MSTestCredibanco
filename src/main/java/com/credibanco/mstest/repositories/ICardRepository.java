package com.credibanco.mstest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.credibanco.mstest.entities.Card;

@Repository
@Component
public interface ICardRepository extends JpaRepository<Card, Long> {

}
