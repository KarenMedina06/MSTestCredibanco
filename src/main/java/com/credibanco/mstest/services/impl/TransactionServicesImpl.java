package com.credibanco.mstest.services.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.credibanco.mstest.entities.Card;
import com.credibanco.mstest.entities.Transaction;
import com.credibanco.mstest.repositories.ITransactionRepository;
import com.credibanco.mstest.services.ITransactionServices;
import com.credibanco.mstest.util.Util;

@Service
public class TransactionServicesImpl implements ITransactionServices{
	
	private static final String SUCCESS = "SUCCESS";
	private static final String CANCEL = "CANCEL";
	
	@Autowired
	private Util util;
	
	@Autowired
	private ITransactionRepository transactionRepository;

	@Override
	public Transaction createTransaction(Card card, Long price) {
		Transaction transaction = new 
				Transaction(util.generateTransactionId(), 
						price, Date.valueOf(LocalDate.now()), 
						SUCCESS, card);
		return transactionRepository.save(transaction);
	}

	@Override
	public Optional<Transaction> getById(Long id) {
		return transactionRepository.findById(id);
	}

	@Override
	public Transaction cancelTransaction(Transaction transaction) {
		transaction.setStatus(CANCEL);
		return transactionRepository.save(transaction);
	}

}
