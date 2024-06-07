package com.credibanco.mstest.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.credibanco.mstest.entities.Card;
import com.credibanco.mstest.entities.Product;
import com.credibanco.mstest.repositories.ICardRepository;
import com.credibanco.mstest.services.ICardServices;
import com.credibanco.mstest.util.Util;

@Service
public class CardServicesImpl implements ICardServices{
	
	public static final String INACTIVE = "INACTIVE";
	public static final String ACTIVE = "ACTIVE";
	
	@Autowired
	private ICardRepository cardRepository;
	
	@Autowired
	private Util util;

	@Override
	public Card generateCardNumber(Product product) {
		Card card = new Card(util.generateCardNumber(String.valueOf(product.getProductId())), 
				product.getClients().getFristName() + " " + product.getClients().getLastName(), 
				util.calculateExpirationDate(), (long) 0, INACTIVE, product, null);
		return cardRepository.save(card);
	}

	@Override
	public Card cardActivation(Card card) {
		card.setStatus(ACTIVE);
		return cardRepository.save(card);
	}

	@Override
	public Card blockCard(Card card) {
		card.setStatus(INACTIVE);
		return cardRepository.save(card);
	}

	@Override
	public Card updateBalance(Card card, Long balance) {
		card.setBalance(balance);
		return cardRepository.save(card);
	}

	@Override
	public Optional<Card> getCardById(Long cardId) {
		return cardRepository.findById(cardId);
	}

}
