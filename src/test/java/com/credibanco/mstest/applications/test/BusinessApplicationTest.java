package com.credibanco.mstest.applications.test;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;

import com.credibanco.mstest.applications.BusinessApplication;
import com.credibanco.mstest.dtos.CardDTO;
import com.credibanco.mstest.dtos.ResponseDTO;
import com.credibanco.mstest.entities.Card;
import com.credibanco.mstest.entities.Product;
import com.credibanco.mstest.entities.Transaction;
import com.credibanco.mstest.exceptions.BadRequestException;
import com.credibanco.mstest.services.impl.CardServicesImpl;
import com.credibanco.mstest.services.impl.ProductServicesImpl;
import com.credibanco.mstest.services.impl.TransactionServicesImpl;
import com.credibanco.mstest.util.Util;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BusinessApplicationTest {

	@Mock
	private ProductServicesImpl productServices;

	@Mock
	private CardServicesImpl cardServices;

	@Mock
	private TransactionServicesImpl transactionServices;

	@Mock
	private Util util;

	@InjectMocks
	private BusinessApplication application;

	private static final String FAILED = "FAILED";
	private static final String CARDID = "cardId";

	@Test
	public void testValidateGenerateNumberCard_Success() {
		Long productId = 1L;
		Product product = new Product();
		Card card = new Card();
		card.setCardId((long) 123456);
		when(productServices.getProductByIdAndAlreadyRegister(productId))
				.thenReturn(Collections.singletonList(product));
		when(cardServices.generateCardNumber(product)).thenReturn(card);
		when(productServices.updateAlreadyRegister(product)).thenReturn(product);
		ResponseDTO<?> response = (ResponseDTO<?>) application.validateGenerateNumberCard(productId);
		assertEquals(HttpStatus.CREATED, response.getResponseCode());
		assertEquals("Numero de Tarjeta generada exitosamente", response.getMessage());
		assertEquals((long)123456, response.getData());
	}

	@Test
	public void testValidateGenerateNumberCard_GenerateCardNumberReturnsNull() {
		Long productId = 1L;
		Product product = new Product();
		when(productServices.getProductByIdAndAlreadyRegister(productId))
				.thenReturn(Collections.singletonList(product));
		when(cardServices.generateCardNumber(product)).thenReturn(null);
		ResponseDTO<?> response = (ResponseDTO<?>) application.validateGenerateNumberCard(productId);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getResponseCode());
		assertEquals(FAILED, response.getMessage());
		assertEquals("No se puede generar el número de la tarjeta", response.getData());
	}

	@Test
	public void testValidateGenerateNumberCard_ExceptionThrown() {
		Long productId = 1L;
		when(productServices.getProductByIdAndAlreadyRegister(productId))
				.thenThrow(new RuntimeException("Test exception"));
		ResponseDTO<?> response = (ResponseDTO<?>) application.validateGenerateNumberCard(productId);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getResponseCode());
		assertEquals(FAILED, response.getMessage());
		assertTrue(response.getData() instanceof RuntimeException);
	}
	
	@Test
    public void testEnrollCard_Success() {
        Long cardId = 1L;
        Card card = new Card();
        card.setCardId(cardId);
        card.setStatus("Active");
        when(cardServices.getCardById(cardId)).thenReturn(Optional.of(card));
        when(cardServices.cardActivation(card)).thenReturn(card);
        ResponseDTO<?> response = (ResponseDTO<?>) application.enrollCard(cardId);
        assertEquals(HttpStatus.OK, response.getResponseCode());
        assertEquals("Tarjeta Activida Correctamente", response.getMessage());
        HashMap<String, String> map = (HashMap<String, String>) response.getData();
        assertEquals(String.valueOf(cardId), map.get(CARDID));
        assertEquals("Active", map.get("status"));
    }

    @Test
    public void testEnrollCard_CardNotFound() {
        Long cardId = 1L;
        when(cardServices.getCardById(cardId)).thenReturn(Optional.empty());
        BadRequestException exception = assertThrows(BadRequestException.class, () -> application.enrollCard(cardId));
        assertEquals("No se encuentra una tarjeta generado con ese número", exception.getMessage());
    }

    @Test
    public void testEnrollCard_ExceptionThrown() {
        Long cardId = 1L;
        Card card = new Card();
        card.setCardId(cardId);
        when(cardServices.getCardById(cardId)).thenReturn(Optional.of(card));
        when(cardServices.cardActivation(card)).thenThrow(new RuntimeException("Test exception"));
        ResponseDTO<?> response = (ResponseDTO<?>) application.enrollCard(cardId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getResponseCode());
        assertEquals(FAILED, response.getMessage());
        assertTrue(response.getData() instanceof RuntimeException);
    }

    @Test
    public void testEnrollCard_CardNotActivated() {
        Long cardId = 1L;
        Card card = new Card();
        card.setCardId(cardId);
        when(cardServices.getCardById(cardId)).thenReturn(Optional.of(card));
        when(cardServices.cardActivation(card)).thenReturn(null);
        ResponseDTO<?> response = (ResponseDTO<?>) application.enrollCard(cardId);
        assertEquals(HttpStatus.GATEWAY_TIMEOUT, response.getResponseCode());
        assertEquals(FAILED, response.getMessage());
        assertEquals("No se puede activar la tarjeta", response.getData());
    }
    
    @Test
    public void testBlockCard_Success() {
        Long cardId = 1L;
        Card card = new Card();
        card.setCardId(cardId);
        when(cardServices.getCardById(cardId)).thenReturn(Optional.of(card));
        when(cardServices.blockCard(card)).thenReturn(null);
        ResponseDTO<?> response = (ResponseDTO<?>) application.blockCard(cardId);
        assertEquals(HttpStatus.OK, response.getResponseCode());
        assertEquals("OK", response.getMessage());
        assertEquals("Tarjeta Bloqueada Exitosamente", response.getData());
    }

    @Test
    public void testBlockCard_CardNotFound() {
        Long cardId = 1L;
        when(cardServices.getCardById(cardId)).thenReturn(Optional.empty());
        BadRequestException exception = assertThrows(BadRequestException.class, () -> application.blockCard(cardId));
        assertEquals("No se encuentra una tarjeta generado con ese número", exception.getMessage());
    }

    @Test
    public void testBlockCard_BlockingFails() {
        Long cardId = 1L;
        Card card = new Card();
        card.setCardId(cardId);
        when(cardServices.getCardById(cardId)).thenReturn(Optional.of(card));
        when(cardServices.blockCard(card)).thenReturn(card); // Simulate blocking failure
        ResponseDTO<?> response = (ResponseDTO<?>) application.blockCard(cardId);
        assertEquals(HttpStatus.GATEWAY_TIMEOUT, response.getResponseCode());
        assertEquals(FAILED, response.getMessage());
        assertEquals("No se pudo bloquear la tarjeta", response.getData());
    }

    @Test
    public void testBlockCard_ExceptionThrown() {
        Long cardId = 1L;
        Card card = new Card();
        card.setCardId(cardId);
        when(cardServices.getCardById(cardId)).thenReturn(Optional.of(card));
        when(cardServices.blockCard(card)).thenThrow(new RuntimeException("Test exception"));
        ResponseDTO<?> response = (ResponseDTO<?>) application.blockCard(cardId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getResponseCode());
        assertEquals(FAILED, response.getMessage());
        assertTrue(response.getData() instanceof RuntimeException);
    }
    
    @Test
    public void testRechargeBalance_Success() {
        Long cardId = 1L;
        Long balance = 100L;
        Card card = new Card();
        card.setCardId(cardId);
        card.setBalance(100L);
        card.setStatus("Active");
        when(cardServices.getCardById(cardId)).thenReturn(Optional.of(card));
        when(cardServices.updateBalance(card, balance)).thenReturn(card);
        ResponseDTO<?> response = (ResponseDTO<?>) application.rechargeBalance(cardId, balance);
        assertEquals(HttpStatus.OK, response.getResponseCode());
        assertEquals("Tarjeta Recargada Exitosamente", response.getMessage());
        HashMap<String, String> responseData = (HashMap<String, String>) response.getData();
        assertEquals(String.valueOf(cardId), responseData.get(CARDID));
        assertEquals(String.valueOf(100L), responseData.get("balance")); // Assuming balance is added to existing balance
    }

    @Test
    public void testRechargeBalance_CardNotFound() {
        Long cardId = 1L;
        Long balance = 100L;
        when(cardServices.getCardById(cardId)).thenReturn(null);
        BadRequestException exception = assertThrows(BadRequestException.class, () -> application.rechargeBalance(cardId, balance));
        assertEquals("No se encuentra una tarjeta generado con ese número", exception.getMessage());
    }

    @Test
    public void testRechargeBalance_InvalidBalance() {
        Long cardId = 1L;
        Long balance = -100L;
        BadRequestException exception = assertThrows(BadRequestException.class, () -> application.rechargeBalance(cardId, balance));
        assertEquals("La recarga de saldo no puede ser menor a 0", exception.getMessage());
    }

    @Test
    public void testRechargeBalance_CardInactive() {
        Long cardId = 1L;
        Long balance = 100L;
        Card card = new Card();
        card.setCardId(cardId);
        card.setStatus("Inactive");
        when(cardServices.getCardById(cardId)).thenReturn(Optional.of(card));
        BadRequestException exception = assertThrows(BadRequestException.class, () -> application.rechargeBalance(cardId, balance));
        assertEquals("La tarjeta no se encuentra activa para realizar la recarga de saldo", exception.getMessage());
    }

    @Test
    public void testRechargeBalance_ExceptionThrown() {
        Long cardId = 1L;
        Long balance = 100L;
        Card card = new Card();
        card.setCardId(cardId);
        card.setStatus("Active");
        when(cardServices.getCardById(cardId)).thenReturn(Optional.of(card));
        when(cardServices.updateBalance(card, balance)).thenThrow(new RuntimeException("Test exception"));
        ResponseDTO<?> response = (ResponseDTO<?>) application.rechargeBalance(cardId, balance);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getResponseCode());
        assertEquals(FAILED, response.getMessage());
        assertTrue(response.getData() instanceof RuntimeException);
    }

    @Test
    public void testRechargeBalance_RechargeFails() {
        Long cardId = 1L;
        Long balance = 100L;
        Card card = new Card();
        card.setCardId(cardId);
        card.setStatus("Active");
        when(cardServices.getCardById(cardId)).thenReturn(Optional.of(card));
        when(cardServices.updateBalance(card, balance)).thenReturn(null);
        ResponseDTO<?> response = (ResponseDTO<?>) application.rechargeBalance(cardId, balance);
        assertEquals(HttpStatus.GATEWAY_TIMEOUT, response.getResponseCode());
        assertEquals(FAILED, response.getMessage());
        assertEquals("No se pudo recargar la tarjeta", response.getData());
    }
    
    @Test
    public void testGetBalance_Success() {
        Long cardId = 1L;
        Card card = new Card();
        card.setCardId(cardId);
        card.setBalance(500L);
        when(cardServices.getCardById(cardId)).thenReturn(Optional.of(card));
        ResponseDTO<?> response = (ResponseDTO<?>) application.getBalance(cardId);
        assertEquals(HttpStatus.OK, response.getResponseCode());
        assertEquals("OK", response.getMessage());
        HashMap<String, String> responseData = (HashMap<String, String>) response.getData();
        assertEquals("500", responseData.get("balance"));
    }

    @Test
    public void testGetBalance_CardNotFound() {
        Long cardId = 1L;
        when(cardServices.getCardById(cardId)).thenReturn(Optional.empty());
        ResponseDTO<?> response = (ResponseDTO<?>) application.getBalance(cardId);
        assertEquals(HttpStatus.OK, response.getResponseCode());
        assertEquals("NOT FOUND", response.getMessage());
        assertEquals("No se encontro una tarjeta asociada", response.getData());
    }
    

    @Test
    public void testMakePurchase_CardNotFound() {
        Long cardId = 1L;
        Long price = 100L;
        when(cardServices.getCardById(cardId)).thenReturn(Optional.empty());
        BadRequestException exception = assertThrows(BadRequestException.class, () -> application.makePurchase(cardId, price));
        assertEquals("No se encuentra una tarjeta generado con ese número", exception.getMessage());
    }

    @Test
    public void testMakePurchase_InsufficientFunds() {
        Long cardId = 1L;
        Long price = 600L;
        Card card = new Card();
        card.setCardId(cardId);
        card.setBalance(500L);
        when(cardServices.getCardById(cardId)).thenReturn(Optional.of(card));
        BadRequestException exception = assertThrows(BadRequestException.class, () -> application.makePurchase(cardId, price));
        assertEquals("Fondos insuficientes", exception.getMessage());
    }

    @Test
    public void testMakePurchase_ExceptionThrown() {
        Long cardId = 1L;
        Long price = 100L;
        Card card = new Card();
        card.setCardId(cardId);
        card.setBalance(500L);
        when(cardServices.getCardById(cardId)).thenReturn(Optional.of(card));
        when(transactionServices.createTransaction(card, price)).thenThrow(new RuntimeException("exception"));
        ResponseDTO<?> response = (ResponseDTO<?>) application.makePurchase(cardId, price);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getResponseCode());
        assertEquals(FAILED, response.getMessage());
        assertTrue(response.getData() instanceof RuntimeException);
    }
    
    @Test
    public void testGetTransaction_Success() {
        Long transactionId = 1L;
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        Card card = new Card();
        card.setCardId(1L);
        card.setBalance(500L);
        transaction.setCard(card);
        transaction.setPrice(100L);
        when(transactionServices.getById(transactionId)).thenReturn(Optional.of(transaction));
        ResponseDTO<?> response = (ResponseDTO<?>) application.getTransaction(transactionId);
        assertEquals(HttpStatus.OK, response.getResponseCode());
        assertEquals("OK", response.getMessage());
        CardDTO responseData = (CardDTO) response.getData();
        assertEquals(card.getCardId(), responseData.getCardId());
        assertEquals(card.getBalance(), responseData.getBalance());
        assertEquals(transaction.getPrice(), responseData.getPrice());
        assertEquals(transaction.getTransactionId(), responseData.getTransactionId());
    }

    @Test
    public void testGetTransaction_NotFound() {
        Long transactionId = 1L;
        when(transactionServices.getById(transactionId)).thenReturn(Optional.empty());
        BadRequestException exception = assertThrows(BadRequestException.class, () -> application.getTransaction(transactionId));
        assertEquals("No se encuentra la transaccion", exception.getMessage());
    }

    @Test
    public void testMakePurchase_TransactionFails() {
        Long cardId = 1L;
        Long price = 100L;
        Card card = new Card();
        card.setCardId(cardId);
        card.setBalance(500L);
        when(cardServices.getCardById(cardId)).thenReturn(Optional.of(card));
        when(transactionServices.createTransaction(card, price)).thenReturn(null);
        ResponseDTO<?> response = (ResponseDTO<?>) application.makePurchase(cardId, price);
        assertEquals(HttpStatus.GATEWAY_TIMEOUT, response.getResponseCode());
        assertEquals(FAILED, response.getMessage());
        assertEquals("No se pudo realizar la transaccion", response.getData());
    }

    @Test
    public void testAnulationTransaction_NotFound() {
        Long cardId = (long)123456;
        Long transactionId = 1L;
        when(cardServices.getCardById(cardId)).thenReturn(Optional.empty());
        BadRequestException exception = assertThrows(BadRequestException.class, () -> application.anulationTransaction(cardId, transactionId));
        assertEquals("No se encuentra una tarjeta generado con ese número", exception.getMessage());
    }

    @Test
    public void testAnulationTransaction_TransactionNotFound() {
        Long cardId = 1L;
        Long transactionId = 1L;
        Card card = new Card();
        card.setCardId(cardId);
        when(cardServices.getCardById(cardId)).thenReturn(Optional.of(card));
        when(transactionServices.getById(transactionId)).thenReturn(Optional.empty());
        BadRequestException exception = assertThrows(BadRequestException.class, () -> application.anulationTransaction(cardId, transactionId));
        assertEquals("No se encuentra la transaccion", exception.getMessage());
    }

    @Test
    public void testAnulationTransaction_TransactionOlderThan24Hours() {
        Long cardId = 1L;
        Long transactionId = 1L;
        Card card = new Card();
        card.setCardId(cardId);
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setPrice(100L);
        transaction.setTransactionDate(Date.valueOf("2024-06-05"));
        when(cardServices.getCardById(cardId)).thenReturn(Optional.of(card));
        when(transactionServices.getById(transactionId)).thenReturn(Optional.of(transaction));
        ResponseDTO<?> exception = (ResponseDTO<?>) application.anulationTransaction(cardId, transactionId);
        assertEquals(FAILED, exception.getMessage());
    }

    @Test
    public void testAnulationTransaction_ExceptionThrown() {
        Long cardId = 1L;
        Long transactionId = 1L;
        LocalDate now = LocalDate.now();
        Card card = new Card();
        card.setCardId(cardId);
        card.setBalance(500L);
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setPrice(100L);
        transaction.setTransactionDate(Date.valueOf(now));
        when(cardServices.getCardById(cardId)).thenReturn(Optional.of(card));
        when(transactionServices.getById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionServices.cancelTransaction(transaction)).thenThrow(new RuntimeException("Test exception"));
        ResponseDTO<?> response = (ResponseDTO<?>) application.anulationTransaction(cardId, transactionId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getResponseCode());
        assertEquals(FAILED, response.getMessage());
        assertTrue(response.getData() instanceof RuntimeException);
    }
}


