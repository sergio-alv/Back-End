package com.ebrozon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ebrozon.model.mensaje;

public interface mensajeRepository  extends CrudRepository<mensaje, Long>{
	
	boolean existsByidentificador(int identificador);
	
	Optional<mensaje> findByidentificador(int identificador);
	
	@Query("Select max(identificador) from mensaje")
	Optional<Integer> lastId();
	
	@Query("Select distinct(emisor) from mensaje where receptor = :un")
	List<String> usuariosChateados(String un);
	
	List<mensaje> findByemisorAndReceptorOrderByIdentificadorAsc(String emisor, String receptor);
	
	List<mensaje> findByidentificadorGreaterThanAndEmisorAndReceptorOrderByIdentificadorAsc(int identificador, String emisor, String receptor);
}
