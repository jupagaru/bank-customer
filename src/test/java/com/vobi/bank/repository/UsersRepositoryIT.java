package com.vobi.bank.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.vobi.bank.domain.UserType;
import com.vobi.bank.domain.Users;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@Slf4j
class UsersRepositoryIT {

	@Autowired
	UsersRepository usersRepository;
	
	@Autowired
	UserTypeRepository userTypeRepository;
	
	@Test
	@Order(1)
	void debeValidarLasDependencias() {
		assertNotNull(usersRepository);
		assertNotNull(userTypeRepository);
	}
	
	@Test
	@Order(2)
	void debeCrearUnUsuario() {
		
		//Arrage
		Integer idUserType = 1;
		
		Users users = null;
		UserType userType = userTypeRepository.findById(idUserType).get();
		
		users = new Users();
		
		users.setEnable("Y");
		users.setUserEmail("jupagaru@gmail.com");
		users.setName("Juan García");
		users.setUserType(userType);
		users.setToken("asdfsdafasdfasdfasdf");
		
		//Act
		
		users = usersRepository.save(users);
		
		//Assert
		
		assertNotNull(users, "El usuario es nulo no se pudo grabar");
	}
	
	@Test
	@Order(3)
	void debeModificarUnUsuario() {
		
		//Arrage
		String idUser = "jupagaru@gmail.com";
		
		Users user = null;
		
		user = usersRepository.findById(idUser).get();
		user.setEnable("N");
		
		//Act
		
		user = usersRepository.save(user);
		
		//Assert
		
		assertNotNull(user, "El usuario es nulo no se pudo Modificar");
	}
	
	@Test
	@Order(4)
	void debeBorrarUnUsuario() {
		
		//Arrage
		String idUser = "jupagaru@gmail.com";
		
		Users user = null;
		Optional<Users> usersOptional = null;
		
		assertTrue(usersRepository.findById(idUser).isPresent(), "No encontró el usuario");
		
		user = usersRepository.findById(idUser).get();
		
		//Act
		usersRepository.delete(user);
		usersOptional=usersRepository.findById(idUser);
		
		//Assert
		
		assertFalse(usersOptional.isPresent(), "No pudo borrar el usuario");
	}
	
	@Test
	@Order(5)
	void debeConsultarTodosLosUsuarios() {
		
		//Arrage
		List<Users> users = null; 
		
		//Act
		users = usersRepository.findAll();
		users.forEach(user->log.info(user.getName()));
		
		//Assert
		assertFalse(users.isEmpty(), "No recuperó información");
		
	}

}

