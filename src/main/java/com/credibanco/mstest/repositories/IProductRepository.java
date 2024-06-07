package com.credibanco.mstest.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.credibanco.mstest.entities.Product;

@Component
public interface IProductRepository extends JpaRepository<Product, Long> {

	@Query(value = "SELECT * FROM products WHERE products.product_id = :productId AND products.already_registered = false", nativeQuery = true)
	List<Product> findByProductIdAndAlreadyRegister(Long productId);
}
