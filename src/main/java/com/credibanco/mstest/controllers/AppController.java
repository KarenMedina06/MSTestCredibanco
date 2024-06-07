package com.credibanco.mstest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.credibanco.mstest.applications.IBusinessApplication;
import com.credibanco.mstest.dtos.CardDTO;
import com.credibanco.mstest.dtos.ResponseDTO;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class AppController {
	
	@Autowired
	private IBusinessApplication application;
	
	@GetMapping(value = "/card/{productId}/number", produces = "application/json")
	@ApiOperation(value = "Resource that allows to generate a card number", response = ResponseDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 200, response = ResponseDTO.class, message = "Generate a card number"),
            @ApiResponse(code = 400, response = ResponseDTO.class, message = "Wrong Request"),
            @ApiResponse(code = 500, response = ResponseDTO.class, message = "Internal System Error"),
            @ApiResponse(code = 504, response = ResponseDTO.class, message = "Gateway timeout") })
	public ResponseEntity<?> generateNumberCard(@PathVariable Long productId){
		return ResponseEntity.ok(application.validateGenerateNumberCard(productId));
	}
	
	@PostMapping(value = "/card/enroll", produces = "application/json")
	@ApiOperation(value = "Resource that allows you to activate a card", response = ResponseDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 200, response = ResponseDTO.class, message = "Activate a card"),
            @ApiResponse(code = 400, response = ResponseDTO.class, message = "Wrong Request"),
            @ApiResponse(code = 500, response = ResponseDTO.class, message = "Internal System Error"),
            @ApiResponse(code = 504, response = ResponseDTO.class, message = "Gateway timeout") })
	public ResponseEntity<?> enrollCard(@RequestBody CardDTO dto){
		return ResponseEntity.ok(application.enrollCard(dto.getCardId()));
	}
	
	@ApiOperation(value = "Resource that allows to block the card", response = ResponseDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 200, response = ResponseDTO.class, message = "Block a Card"),
            @ApiResponse(code = 400, response = ResponseDTO.class, message = "Wrong Request"),
            @ApiResponse(code = 500, response = ResponseDTO.class, message = "Internal System Error"),
            @ApiResponse(code = 504, response = ResponseDTO.class, message = "Gateway timeout") })
	@DeleteMapping(value = "/card/{cardId}", produces = "application/json")
	public ResponseEntity<?> blockNumberCard(@PathVariable Long cardId){
		return ResponseEntity.ok(application.blockCard(cardId));
	}
	
	@ApiOperation(value = "Resource that allows to recharge a card", response = ResponseDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 200, response = ResponseDTO.class, message = "Recharge a card"),
            @ApiResponse(code = 400, response = ResponseDTO.class, message = "Wrong Request"),
            @ApiResponse(code = 500, response = ResponseDTO.class, message = "Internal System Error"),
            @ApiResponse(code = 504, response = ResponseDTO.class, message = "Gateway timeout") })
	@PostMapping(value = "/card/balance", produces = "application/json")
	public ResponseEntity<?> rechargeBalance(@RequestBody CardDTO dto){
		return ResponseEntity.ok(application.rechargeBalance(dto.getCardId(), dto.getBalance()));
	}
	
	@ApiOperation(value = "Resource to consult a card", response = ResponseDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 200, response = ResponseDTO.class, message = "Get a card"),
            @ApiResponse(code = 400, response = ResponseDTO.class, message = "Wrong Request"),
            @ApiResponse(code = 500, response = ResponseDTO.class, message = "Internal System Error"),
            @ApiResponse(code = 504, response = ResponseDTO.class, message = "Gateway timeout") })
	@GetMapping(value = "/card/balance/{cardId}", produces = "application/json")
	public ResponseEntity<?> getBalanceCard(@PathVariable Long cardId){
		return ResponseEntity.ok(application.getBalance(cardId));
	}
	
	@ApiOperation(value = "Resource that allows you to perform a purchase transaction", response = ResponseDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 200, response = ResponseDTO.class, message = "Purchase Transaction"),
            @ApiResponse(code = 400, response = ResponseDTO.class, message = "Wrong Request"),
            @ApiResponse(code = 500, response = ResponseDTO.class, message = "Internal System Error"),
            @ApiResponse(code = 504, response = ResponseDTO.class, message = "Gateway timeout") })
	@PostMapping(value = "/transaction/purchase", produces = "application/json")
	public ResponseEntity<?> makePurchase(@RequestBody CardDTO dto){
		return ResponseEntity.ok(application.makePurchase(dto.getCardId(), dto.getPrice()));
	}
	
	@ApiOperation(value = "Resource to consult a transaction", response = ResponseDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 200, response = ResponseDTO.class, message = "Get a transaction"),
            @ApiResponse(code = 400, response = ResponseDTO.class, message = "Wrong Request"),
            @ApiResponse(code = 500, response = ResponseDTO.class, message = "Internal System Error"),
            @ApiResponse(code = 504, response = ResponseDTO.class, message = "Gateway timeout") })
	@GetMapping(value = "/transaction/{transactionId}", produces = "application/json")
	public ResponseEntity<?> getTransaction(@PathVariable Long transactionId){
		return ResponseEntity.ok(application.getTransaction(transactionId));
	}
	
	@ApiOperation(value = "Resource that allows you to cancel a transaction within 24 hours", response = ResponseDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 200, response = ResponseDTO.class, message = "Cancel a transaction"),
            @ApiResponse(code = 400, response = ResponseDTO.class, message = "Wrong Request"),
            @ApiResponse(code = 500, response = ResponseDTO.class, message = "Internal System Error"),
            @ApiResponse(code = 504, response = ResponseDTO.class, message = "Gateway timeout") })
	@PostMapping(value = "/transaction/anulation", produces = "application/json")
	public ResponseEntity<?> anulationTransaction(@RequestBody CardDTO dto){
		return ResponseEntity.ok(application.anulationTransaction(dto.getCardId(), dto.getTransactionId()));
	}
	

}
