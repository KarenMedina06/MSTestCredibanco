package com.credibanco.mstest.services;

import java.util.List;

import com.credibanco.mstest.entities.Product;

public interface IProductServices {

	public List<Product> getProductByIdAndAlreadyRegister(Long productId);
	
	public Product updateAlreadyRegister(Product product);
}
