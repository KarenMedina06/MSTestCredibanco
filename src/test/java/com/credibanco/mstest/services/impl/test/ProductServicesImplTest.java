package com.credibanco.mstest.services.impl.test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.credibanco.mstest.entities.Clients;
import com.credibanco.mstest.entities.Product;
import com.credibanco.mstest.repositories.IProductRepository;
import com.credibanco.mstest.services.impl.ProductServicesImpl;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProductServicesImplTest {
	
	private List<Product> myList;
	
	@Mock
	private IProductRepository repository;
	
	@InjectMocks
	private ProductServicesImpl servicesImpl;
	
	@BeforeEach
	public void setUp() {
		 myList = new ArrayList<>();
	}
	
	
	@Test
	void getProductByIdAndAlreadyRegisterTest() {
		Clients clientMock = new Clients((long) 1234, "Jhon", "Doe", "3203879", null);
		Product productMock = new Product((long) 1, (long)102030, "Tarjeta Debito", false, null, clientMock);
        myList.add(productMock);
		when(repository.findByProductIdAndAlreadyRegister((long) 102030)).thenReturn(myList);
		List<Product> products = servicesImpl.getProductByIdAndAlreadyRegister((long) 102030);
		assertEquals(1, products.size());
		verify(repository).findByProductIdAndAlreadyRegister((long) 102030);
	}
	
	@Test
	void updateAlreadyRegisterTest() {
		Clients clientMock = new Clients((long) 1234, "Jhon", "Doe", "3203879", null);
		Optional<Product> productMock = Optional.ofNullable(new Product((long) 1, (long)102030, "Tarjeta Debito", false, null, clientMock));
		when(repository.findById((long) 1)).thenReturn(productMock);
		servicesImpl.updateAlreadyRegister(productMock.get());
		assertEquals(true, productMock.get().getAlreadyRegistered());
		verify(repository, times(1)).save(productMock.get());
		
	}

}
