package com.ebrozon.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ebrozon.model.archivo;

public interface archivoRepository  extends CrudRepository<archivo, Long>{
	
	boolean existsByidentificador(int identificador);
	
	Optional<archivo> findByidentificador(int identificador);
	
	Optional<archivo> findByurl(String url);
}
