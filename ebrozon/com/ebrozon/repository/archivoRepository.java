package com.ebrozon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ebrozon.model.archivo;

public interface archivoRepository  extends CrudRepository<archivo, Long>{
	
	boolean existsByidentificador(int identificador);
	
	Optional<archivo> findByidentificador(int identificador);
	
	Optional<archivo> findByurl(String url);
	
	@Query("Select max(identificador) from archivo")
	Optional<Integer> lastId();
}
