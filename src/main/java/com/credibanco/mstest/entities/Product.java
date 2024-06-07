package com.credibanco.mstest.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "products")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable{
	
	private static final long serialVersionUID = 6998500906816037882L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "productId", length = 6, nullable = false)
	private Long productId;
	
	@Column(name = "productName")
	private String productName;
	
	@Column(name = "alreadyRegistered")
	private Boolean alreadyRegistered;
	
	@OneToMany(mappedBy = "product")
    private List<Card> cards;
	
	@ManyToOne
    @JoinColumn(name = "clientId")
    private Clients clients;

}
