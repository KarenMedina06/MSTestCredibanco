package com.credibanco.mstest.entities;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "transactions")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction implements Serializable{
	
	private static final long serialVersionUID = -3972525257934579963L;
	
	@Id
	@Column(name = "transactionId", nullable = false)
	private Long transactionId;
	
	@Column(name = "price", nullable = false)
	private Long price;
	
	@Column(name = "transactionDate")
	private Date transactionDate;
	
	@Column(name = "status")
	private String status;
	
	@ManyToOne
    @JoinColumn(name = "cardId")
    private Card card;

}
