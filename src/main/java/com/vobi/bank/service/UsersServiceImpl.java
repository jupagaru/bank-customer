package com.vobi.bank.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vobi.bank.domain.Users;
import com.vobi.bank.repository.UsersRepository;

@Service
public class UsersServiceImpl implements UsersService{
	
	@Autowired
	UsersRepository usersRepository;
	
	@Autowired
	Validator validator;

	@Override
	@Transactional(readOnly = true)
	public List<Users> findAll() {
		return usersRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Users> findById(String id) {
		return usersRepository.findById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Long count() {
		return usersRepository.count();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Users save(Users entity) throws Exception {
		if (entity == null) {
			throw new Exception("El user es nulo");
		}
		validate(entity);
		if(usersRepository.existsById(entity.getUserEmail())) {
			throw new Exception ("El usuario ya existe");
		}
		return usersRepository.save(entity);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Users update(Users entity) throws Exception {
		if (entity == null) {
			throw new Exception("El usuario es nulo");
		}

		validate(entity);

		if (usersRepository.existsById(entity.getUserEmail()) == false) {
			throw new Exception("El usuario no existe");
		}

		return usersRepository.save(entity);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(Users entity) throws Exception {
		if (entity == null) {
			throw new Exception("El customer es nulo");
		}

		if (entity.getUserEmail() == null) {
			throw new Exception("El user id es nulo");
		}

		if (usersRepository.existsById(entity.getUserEmail()) == false) {
			throw new Exception("El usuario no existe");
		}
		// debemos validar que el id que deseamos eliminar no se encuentre en otras
		// tablas
		// para evitar errores de constraint. Relación de tablas
		findById(entity.getUserEmail()).ifPresent(users -> {
			if (users.getTransactions() != null && users.getTransactions().isEmpty() == false) {
				throw new RuntimeException("El usuario tiene transacciones asociadas");
			}
		});

		usersRepository.deleteById(entity.getUserEmail());
		
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteById(String id) throws Exception {
		if(id == null) {
			throw new Exception("El id es nulo");
		}
		
		if(usersRepository.existsById(id) == false) {
			throw new Exception("El usuario no existe");
		}
		delete(usersRepository.findById(id).get());
		
	}

	@Override
	public void validate(Users entity) throws Exception {
		Set<ConstraintViolation<Users>> constraintViolations = validator.validate(entity);
		if (constraintViolations.isEmpty() == false) {
			throw new ConstraintViolationException(constraintViolations);
		}
		
	}

	

}
