package com.ebrozon.repository;
 
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ebrozon.model.etiqueta;

public interface etiquetaRepository extends CrudRepository<etiqueta, Long>{
	boolean existsBynombreetiqueta(String nombreetiqueta);
	
	Optional<etiqueta> findBynombre(String nombre);
}