package com.credibanco.mstest.services;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.credibanco.mstest.entities.Card;
import com.credibanco.mstest.entities.Transaction;

@Component
public interface ITransactionServices {

	public Transaction createTransaction(Card card, Long price);
	
	public Optional<Transaction> getById(Long id);
	
	public Transaction cancelTransaction(Transaction transaction);
}
