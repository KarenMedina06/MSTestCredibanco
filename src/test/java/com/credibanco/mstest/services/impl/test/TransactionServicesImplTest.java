package com.credibanco.mstest.services.impl.test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import com.credibanco.mstest.entities.Card;
import com.credibanco.mstest.entities.Clients;
import com.credibanco.mstest.entities.Product;
import com.credibanco.mstest.entities.Transaction;
import com.credibanco.mstest.repositories.ITransactionRepository;
import com.credibanco.mstest.services.impl.TransactionServicesImpl;
import com.credibanco.mstest.util.Util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServicesImplTest {

	@Mock
	private ITransactionRepository repository;
	
	@InjectMocks
	private TransactionServicesImpl impl;
	
	@Mock
	private Util util;
	
	@Test
	void createTransactionTest() {
		Clients clientMock = new Clients((long) 1234, "Jhon", "Doe", "3203879", null);
		Product productMock = new Product((long) 1, (long)102030, "Tarjeta Debito", false, null, clientMock);
		Card cardMock = new Card(util.generateCardNumber(String.valueOf(productMock.getProductId())), "Jhon Doe", "06/27", (long) 10000, "ACTIVE", productMock, null);
		Transaction transactionMock = new Transaction(util.generateTransactionId(), (long)100, Date.valueOf(LocalDate.now()) , "SUCESS", cardMock);
		Long long1 = (long) 0;
		when(util.generateTransactionId()).thenReturn(long1);
		when(repository.save(any(Transaction.class))).thenReturn(transactionMock);
		Transaction createdTransaction = impl.createTransaction(cardMock, (long)100);
		verify(repository, times(1)).save(any(Transaction.class));
		ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(repository).save(transactionCaptor.capture());
        Transaction capturedTransaction = transactionCaptor.getValue();
        assertEquals("SUCCESS", capturedTransaction.getStatus());
        assertEquals(transactionMock, createdTransaction);
	}
	
	@Test
	void getByIdTest() {
		Clients clientMock = new Clients((long) 1234, "Jhon", "Doe", "3203879", null);
		Product productMock = new Product((long) 1, (long)102030, "Tarjeta Debito", false, null, clientMock);
		Card cardMock = new Card(Long.parseLong("1020301234567890"), "Jhon Doe", "06/27", (long) 10000, "ACTIVE", productMock, null);
		Optional<Transaction> transactionMock = Optional.of(new Transaction((long) 876543, (long)100, Date.valueOf(LocalDate.now()) , "SUCESS", cardMock));
		when(repository.findById((long) 876543)).thenReturn(transactionMock);
		Optional<Transaction> transaction = impl.getById((long) 876543);
		assertEquals(876543, transaction.get().getTransactionId());
		verify(repository).findById((long) 876543);
	}
	
	@Test
	void cancelTransactionTest() {
		Clients clientMock = new Clients((long) 1234, "Jhon", "Doe", "3203879", null);
		Product productMock = new Product((long) 1, (long)102030, "Tarjeta Debito", false, null, clientMock);
		Card cardMock = new Card(Long.parseLong("1020301234567890"), "Jhon Doe", "06/27", (long) 10000, "ACTIVE", productMock, null);
		Transaction transactionMock = new Transaction((long) 876543, (long)100, Date.valueOf(LocalDate.now()) , "SUCESS", cardMock);
		impl.cancelTransaction(transactionMock);
		assertEquals("CANCEL", transactionMock.getStatus());
		verify(repository, times(1)).save(transactionMock);
	}
}
