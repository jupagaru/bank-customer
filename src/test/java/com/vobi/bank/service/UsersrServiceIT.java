package com.vobi.bank.service;

import static org.junit.jupiter.api.Assertions.*;

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
import com.vobi.bank.repository.UserTypeRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@Slf4j
class UsersrServiceIT {

	@Autowired
	UsersService usersService;

	@Autowired
	UserTypeRepository usersTypeRepository;

	@Test
	@Order(1)
	void debeValidarLasDependencias() {
		assertNotNull(usersService);
		assertNotNull(usersTypeRepository);
	}

	@Test
	@Order(2)
	void debeCrearUnUsuario() throws Exception {

		// Arrage
		Integer idUserType = 1;

		Users users = null;
		UserType userType = usersTypeRepository.findById(idUserType).get();

		users = new Users();
		users.setEnable("Y");
		users.setUserEmail("jupagaru@gmail.com");
		users.setName("Juan García");
		users.setUserType(userType);
		users.setToken("asdfsdafasdfasdfasdf");

		// Act

		users = usersService.save(users);

		// Assert

		assertNotNull(users, "El usuario es nulo no se pudo grabar");
	}

	@Test
	@Order(3)
	void debeModificarUnUsuario() throws Exception {

		// Arrage
		String idUser = "jupagaru@gmail.com";

		Users user = null;

		user = usersService.findById(idUser).get();
		user.setEnable("N");

		// Act

		user = usersService.update(user);

		// Assert

		assertNotNull(user, "El usuario es nulo no se pudo Modificar");
	}

	@Test
	@Order(4)
	void debeBorrarUnUsuario() throws Exception {

		// Arrage
		String idUser = "jupagaru@gmail.com";

		Users user = null;
		Optional<Users> usersOptional = null;

		assertTrue(usersService.findById(idUser).isPresent(), "No encontró el usuario");

		user = usersService.findById(idUser).get();

		// Act
		usersService.delete(user);
		usersOptional = usersService.findById(idUser);

		// Assert

		assertFalse(usersOptional.isPresent(), "No pudo borrar el usuario");
	}

	@Test
	@Order(5)
	void debeConsultarTodosLosUsuarios() {

		// Arrage
		List<Users> users = null;

		// Act
		users = usersService.findAll();
		users.forEach(user -> log.info(user.getName()));

		// Assert
		assertFalse(users.isEmpty(), "No recuperó información");

	}

}
