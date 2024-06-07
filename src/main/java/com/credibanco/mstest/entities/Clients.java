package com.credibanco.mstest.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "clients")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Clients implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 6175958646448744897L;
	
	@Id
	@Column(name = "clientId", nullable = false)
	private Long clientId;
	
	@Column(name = "fristName")
	private String fristName;
	
	@Column(name = "lastName")
	private String lastName;
	
	@Column(name = "cellphone")
	private String cellphone;
	
	@OneToMany(mappedBy = "clients")
    private List<Product> product;

}
