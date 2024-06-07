package com.credibanco.mstest.applications;

import org.springframework.stereotype.Component;

@Component
public interface IBusinessApplication {

	public Object validateGenerateNumberCard(Long productId);
	
	public Object enrollCard(Long cardId);
	
	public Object blockCard(Long cardId);
	
	public Object rechargeBalance(Long cardId, Long balance);
	
	public Object getBalance(Long cardId);
	
	public Object makePurchase(Long cardId, Long price);
	
	public Object getTransaction(Long transactionId);
	
	public Object anulationTransaction(Long cardId, Long transactionId);
}
