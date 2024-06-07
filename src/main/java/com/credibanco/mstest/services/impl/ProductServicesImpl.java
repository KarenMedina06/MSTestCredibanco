package com.credibanco.mstest.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.credibanco.mstest.entities.Product;
import com.credibanco.mstest.repositories.IProductRepository;
import com.credibanco.mstest.services.IProductServices;

@Service
public class ProductServicesImpl implements IProductServices{
	
	@Autowired
	private IProductRepository productRepository;

	@Override
	public List<Product> getProductByIdAndAlreadyRegister(Long productId) {
		return productRepository.findByProductIdAndAlreadyRegister(productId);
	}

	@Override
	public Product updateAlreadyRegister(Product product) {
		product.setAlreadyRegistered(true);
		return productRepository.save(product);
	}

	
}
