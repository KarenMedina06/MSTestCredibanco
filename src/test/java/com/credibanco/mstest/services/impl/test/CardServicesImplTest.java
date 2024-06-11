package com.credibanco.mstest.services.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.credibanco.mstest.repositories.ICardRepository;
import com.credibanco.mstest.services.impl.CardServicesImpl;
import com.credibanco.mstest.util.Util;

@ExtendWith(MockitoExtension.class)
public class CardServicesImplTest {
	
	@Mock
	private ICardRepository repository;
	
	@InjectMocks
	private CardServicesImpl impl;
	
	@Mock
	private Util util;
	
	@Test
	void testGenerateCardNumber() {
		Clients clientMock = new Clients((long) 1234, "Jhon", "Doe", "3203879", null);
		Product productMock = new Product((long) 1, (long)102030, "Tarjeta Debito", false, null, clientMock);
		Card cardMock = new Card(util.generateCardNumber(String.valueOf(productMock.getProductId())), "Jhon Doe", "06/27", (long) 0, "ACTIVE", productMock, null);
		when(repository.save(any(Card.class))).thenReturn(cardMock);
		Card createdCard = impl.generateCardNumber(productMock);
		verify(repository, times(1)).save(any(Card.class));
		ArgumentCaptor<Card> cardCaptor = ArgumentCaptor.forClass(Card.class);
        verify(repository).save(cardCaptor.capture());
        Card capturedCard = cardCaptor.getValue();
        assertEquals("INACTIVE", capturedCard.getStatus());
        assertEquals(cardMock, createdCard);
	}
	
	@Test
	void testGetCardById() {
		Clients clientMock = new Clients((long) 1234, "Jhon", "Doe", "3203879", null);
		Product productMock = new Product((long) 1, (long)102030, "Tarjeta Debito", false, null, clientMock);
		Optional<Card> cardMock = Optional.of(new Card(util.generateCardNumber(String.valueOf(productMock.getProductId())), "Jhon Doe", "06/27", (long) 0, "INACTIVE", productMock, null));
		Long long1 = cardMock.get().getCardId();
		when(repository.findById(long1)).thenReturn(cardMock);
		Optional<Card> card = impl.getCardById(cardMock.get().getCardId());
		assertEquals("INACTIVE", card.get().getStatus());
		verify(repository).findById(cardMock.get().getCardId());
	}
	
	@Test
	void testCardActivation() {
		Clients clientMock = new Clients((long) 1234, "Jhon", "Doe", "3203879", null);
		Product productMock = new Product((long) 1, (long)102030, "Tarjeta Debito", false, null, clientMock);
		Card cardMock = new Card(util.generateCardNumber(String.valueOf(productMock.getProductId())), "Jhon Doe", "06/27", (long) 0, "ACTIVE", productMock, null);
		when(repository.save(any(Card.class))).thenReturn(cardMock);
		Card card = impl.cardActivation(cardMock);
		assertEquals("ACTIVE", card.getStatus());
		verify(repository, times(1)).save(cardMock);
	}
	
	@Test
	void testBlockCard() {
		Clients clientMock = new Clients((long) 1234, "Jhon", "Doe", "3203879", null);
		Product productMock = new Product((long) 1, (long)102030, "Tarjeta Debito", false, null, clientMock);
		Card cardMock = new Card(util.generateCardNumber(String.valueOf(productMock.getProductId())), "Jhon Doe", "06/27", (long) 0, "ACTIVE", productMock, null);
		when(repository.save(any(Card.class))).thenReturn(cardMock);
		Card card = impl.blockCard(cardMock);
		assertEquals("INACTIVE", card.getStatus());
		verify(repository, times(1)).save(cardMock);
	}
	
	@Test
	void testUpdateBalance() {
		Clients clientMock = new Clients((long) 1234, "Jhon", "Doe", "3203879", null);
		Product productMock = new Product((long) 1, (long)102030, "Tarjeta Debito", false, null, clientMock);
		Card cardMock = new Card(util.generateCardNumber(String.valueOf(productMock.getProductId())), "Jhon Doe", "06/27", (long) 0, "ACTIVE", productMock, null);
		when(repository.save(any(Card.class))).thenReturn(cardMock);
		Card card = impl.updateBalance(cardMock, Long.parseLong("10000"));
		assertEquals("ACTIVE", card.getStatus());
		verify(repository, times(1)).save(cardMock);
	}

}
