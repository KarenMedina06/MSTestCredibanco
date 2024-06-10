package com.credibanco.mstest.applications;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.credibanco.mstest.dtos.CardDTO;
import com.credibanco.mstest.dtos.ResponseDTO;
import com.credibanco.mstest.entities.Card;
import com.credibanco.mstest.entities.Product;
import com.credibanco.mstest.entities.Transaction;
import com.credibanco.mstest.exceptions.BadRequestException;
import com.credibanco.mstest.services.ICardServices;
import com.credibanco.mstest.services.IProductServices;
import com.credibanco.mstest.services.ITransactionServices;

@Service
public class BusinessApplication implements IBusinessApplication {

	public static final String ERROR_GET_CARD = "No se encuentra una tarjeta generado con ese número";
	public static final String FAILED = "FAILED";
	public static final String CARDID = "cardId";

	@Autowired
	private ICardServices services;

	@Autowired
	private IProductServices productServices;

	@Autowired
	private ITransactionServices transactionServices;

	@Override
	public Object validateGenerateNumberCard(Long productId) {
		try {
			List<Product> product = productServices.getProductByIdAndAlreadyRegister(productId);
			Card card = services.generateCardNumber(product.get(0));
			if (Objects.nonNull(card) && Objects.nonNull(productServices.updateAlreadyRegister(product.get(0)))) {
				return new ResponseDTO<>(HttpStatus.CREATED, "Numero de Tarjeta generada exitosamente",
						card.getCardId());
			}
		} catch (Exception e) {
			return new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR, FAILED, e);
		}

		return new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR, FAILED,
				"No se puede generar el número de la tarjeta");
	}

	@Override
	public Object enrollCard(Long cardId) {
		Optional<Card> card = services.getCardById(cardId);
		if (card.isEmpty()) {
			throw new BadRequestException(ERROR_GET_CARD);
		}
		try {
			Card updateCard = card.get();
			if (Objects.nonNull(services.cardActivation(updateCard))) {
				HashMap<String, String> map = new HashMap<>();
				map.put(CARDID, String.valueOf(updateCard.getCardId()));
				map.put("status", updateCard.getStatus());
				return new ResponseDTO<>(HttpStatus.OK, "Tarjeta Activida Correctamente", map);
			}
		} catch (Exception e) {
			return new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR, FAILED, e);
		}
		return new ResponseDTO<>(HttpStatus.GATEWAY_TIMEOUT, FAILED, "No se puede activar la tarjeta");
	}

	@Override
	public Object blockCard(Long cardId) {
		Optional<Card> card = services.getCardById(cardId);
		if (card.isEmpty()) {
			throw new BadRequestException(ERROR_GET_CARD);
		}
		try {
			if (Objects.isNull(services.blockCard(card.get()))) {
				return new ResponseDTO<>(HttpStatus.OK, "OK", "Tarjeta Bloqueada Exitosamente");
			}
		} catch (Exception e) {
			return new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR, FAILED, e);
		}
		return new ResponseDTO<>(HttpStatus.GATEWAY_TIMEOUT, FAILED, "No se pudo bloquear la tarjeta");
	}

	@Override
	public Object rechargeBalance(Long cardId, Long balance) {
		HashMap<String, String> response = new HashMap<>();
		Optional<Card> card = services.getCardById(cardId);
		if (Objects.isNull(card)) {
			throw new BadRequestException(ERROR_GET_CARD);
		}
		if (balance < 0) {
			throw new BadRequestException("La recarga de saldo no puede ser menor a 0");
		}
		if (card.isPresent() && card.get().getStatus().equals("Inactive")) {
			throw new BadRequestException("La tarjeta no se encuentra activa para realizar la recarga de saldo");
		}
		try {
			if (card.isPresent()) {
				Card rechargeCard = card.get();
				if (Objects.nonNull(services.updateBalance(rechargeCard, balance))) {
					response.put(CARDID, String.valueOf(rechargeCard.getCardId()));
					response.put("balance", String.valueOf(rechargeCard.getBalance()));
					return new ResponseDTO<>(HttpStatus.OK, "Tarjeta Recargada Exitosamente", response);
				}
			}
		} catch (Exception e) {
			return new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR, FAILED, e);
		}
		return new ResponseDTO<>(HttpStatus.GATEWAY_TIMEOUT, FAILED, "No se pudo recargar la tarjeta");
	}

	@Override
	public Object getBalance(Long cardId) {
		HashMap<String, String> response = new HashMap<>();
		Optional<Card> card = services.getCardById(cardId);
		if (card.isEmpty()) {
			return new ResponseDTO<>(HttpStatus.OK, "NOT FOUND", "No se encontro una tarjeta asociada");
		} else {
			response.put("balance", String.valueOf(card.get().getBalance()));
			return new ResponseDTO<>(HttpStatus.OK, "OK", response);
		}
	}

	@Override
	public Object makePurchase(Long cardId, Long price) {
		Optional<Card> card = services.getCardById(cardId);
		if (card.isEmpty()) {
			throw new BadRequestException(ERROR_GET_CARD);
		}
		if (price > card.get().getBalance()) {
			throw new BadRequestException("Fondos insuficientes");
		}
		try {
			Long newBalance = card.get().getBalance() - price;
			Transaction transaction = transactionServices.createTransaction(card.get(), price);
			if (Objects.nonNull(transaction)) {
				Card newCard = services.updateBalance(card.get(), newBalance);
				CardDTO dto = new CardDTO(cardId, newBalance, price, transaction.getTransactionId());
				if (newCard.getBalance().equals(newBalance)) {
					return new ResponseDTO<>(HttpStatus.CREATED, "Transacción Exitosa", dto);
				}
			}
		} catch (Exception e) {
			return new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR, FAILED, e);
		}
		return new ResponseDTO<>(HttpStatus.GATEWAY_TIMEOUT, FAILED, "No se pudo realizar la transaccion");
	}

	@Override
	public Object getTransaction(Long transactionId) {
		Optional<Transaction> transaction = transactionServices.getById(transactionId);
		if (transaction.isEmpty()) {
			throw new BadRequestException("No se encuentra la transaccion");
		}
		try {
			CardDTO dto = new CardDTO(transaction.get().getCard().getCardId(), transaction.get().getCard().getBalance(),
					transaction.get().getPrice(), transaction.get().getTransactionId());
			return new ResponseDTO<>(HttpStatus.OK, "OK", dto);
		} catch (Exception e) {
			return new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR, FAILED, e);
		}
	}

	@Override
	public Object anulationTransaction(Long cardId, Long transactionId) {
		LocalDate now = LocalDate.now();
		Optional<Card> card = services.getCardById(cardId);
		if (card.isEmpty()) {
			throw new BadRequestException(ERROR_GET_CARD);
		}
		Optional<Transaction> transaction = transactionServices.getById(transactionId);
		if (transaction.isEmpty()) {
			throw new BadRequestException("No se encuentra la transaccion");
		}
		try {
			LocalDate targetDateTime = Instant.ofEpochMilli(transaction.get().getTransactionDate().getTime())
					.atZone(ZoneId.systemDefault()).toLocalDate();
			if (targetDateTime != now && targetDateTime.isBefore(now)) {
				throw new BadRequestException("No se puede anular la transaccion, no debe ser mayor a 24 horas");
			}
			if (Objects.nonNull(transactionServices.cancelTransaction(transaction.get()))) {
				Card updateCard = card.get();
				Long lastBalance = updateCard.getBalance();
				Long returnBalance = transaction.get().getPrice();
				Long newBalance = returnBalance + lastBalance;
				Card newCard = services.updateBalance(card.get(), newBalance);
				if (newCard.getBalance().equals(newBalance)) {
					HashMap<String, Long> map = new HashMap<>();
					map.put(CARDID, newCard.getCardId());
					map.put("last balance", card.get().getBalance());
					map.put("new balance", newCard.getBalance());
					return new ResponseDTO<>(HttpStatus.OK, "Transacción Anulada Exitosa", map);
				}
			}
		} catch (Exception e) {
			return new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR, FAILED, e);
		}
		return new ResponseDTO<>(HttpStatus.GATEWAY_TIMEOUT, FAILED, "No se pudo anular la transaccion");
	}

}
