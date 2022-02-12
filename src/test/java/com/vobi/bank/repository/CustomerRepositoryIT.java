package com.vobi.bank.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.vobi.bank.domain.Customer;
import com.vobi.bank.domain.DocumentType;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class CustomerRepositoryIT {
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	DocumentTypeRepository documentTypeRepository;
	
	@Test
	@Order(1)
	void debeValidarLasDependencias() {
		assertNotNull(customerRepository);
		assertNotNull(documentTypeRepository);
	}
	
	@Test
	@Order(2)
	void debeCrearUnCustomer() {
		
		//Arrage
		Integer iddocumentType = 1;
		Integer idCustomer = 14836554;
		
		Customer customer = null;
		DocumentType documentType = documentTypeRepository.findById(iddocumentType).get();
		
		customer = new Customer();
		customer.setAddress("Avenida siempre viva 123");
		customer.setCustId(idCustomer);
		customer.setDocumentType(documentType);
		customer.setEmail("hjsimposon@gmail.com");
		customer.setEnable("Y");
		customer.setName("Homero J Simpson");
		customer.setPhone("5555555555");
		customer.setToken("asdfsdafasdfasdfasdf");
		
		//Act
		
		customer = customerRepository.save(customer);
		
		//Assert
		
		assertNotNull(customer, "El customer es nulo no se pudo grabar");
	}
	
	@Test
	@Order(3)
	void debeModificarUnCustomer() {
		
		//Arrage
		Integer idCustomer = 14836554;
		
		Customer customer = null;
		
		customer = customerRepository.findById(idCustomer).get();
		customer.setEnable("N");
		
		//Act
		
		customer = customerRepository.save(customer);
		
		//Assert
		
		assertNotNull(customer, "El customer es nulo no se pudo Modificar");
	}
	
	@Test
	@Order(4)
	void debeBorrarUnCustomer() {
		
		//Arrage
		Integer idCustomer = 14836554;
		
		Customer customer = null;
		Optional<Customer> customerOptional = null;
		
		assertTrue(customerRepository.findById(idCustomer).isPresent(), "No encontró el customer");
		
		customer = customerRepository.findById(idCustomer).get();
		
		//Act
		customerRepository.delete(customer);
		customerOptional=customerRepository.findById(idCustomer);
		
		//Assert
		
		assertFalse(customerOptional.isPresent(), "No pudo borrar el customer");
	}

}
