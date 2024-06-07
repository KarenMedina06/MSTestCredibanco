package com.credibanco.mstest.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "cards")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card implements Serializable{
	
	private static final long serialVersionUID = 4420941184988435966L;
	
	@Id
	@Column(name = "cardId", length = 16, nullable = false, unique = true)
	private Long cardId;
	
	@Column(name = "ownerName")
	private String ownerName;
	
	@Column(name = "expirationDate")
	private String expirationDate;
	
	@Column(name = "balance")
	private Long balance;
	
	@Column(name = "status")
	private String status;
	
	@ManyToOne
    @JoinColumn(name = "idProduct")
    private Product product;
	
	@OneToMany(mappedBy = "card")
    private List<Transaction> transactions;

}
