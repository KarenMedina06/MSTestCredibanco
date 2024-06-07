package com.credibanco.mstest.services;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.credibanco.mstest.entities.Card;
import com.credibanco.mstest.entities.Product;

@Component
public interface ICardServices {

	public Card generateCardNumber(Product product);
	
	public Card cardActivation(Card card);
	
	public Card blockCard(Card card);
	
	public Card updateBalance(Card card, Long balance);
	
	public Optional<Card> getCardById(Long cardId);

}
