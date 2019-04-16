package com.ebrozon.repository;
 
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ebrozon.model.usuario;

public interface usuarioRepository extends CrudRepository<usuario, Long>{
	
	boolean existsBynombreusuario(String nombreusuario);
	
	Optional<usuario> findBynombreusuario(String nombreusuario);
	
	boolean existsBycorreo(String correo);
	
	Optional<usuario> findBycorreo(String correo);
	
	@Modifying
	@Transactional
	@Query(value = "insert into registrologins(usuario) values(:un)", nativeQuery = true)
	void registrarLogin(String un);
}