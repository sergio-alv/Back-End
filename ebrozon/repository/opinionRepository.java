package com.ebrozon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ebrozon.model.opinion;

public interface opinionRepository  extends CrudRepository<opinion, Long>{
	
	boolean existsByidentificador(int identificador);
	
	Optional<opinion> findByidentificador(int identificador);
	
	@Query("Select max(identificador) from opinion")
	Optional<Integer> lastId();
	
	@Query("Select avg(estrellas) from opinion where receptor = :un")
	double mediaEstrellasUsuario(String un);
	
	List<opinion> findByemisorOrderByIdentificadorDesc(String emisor);
	
	List<opinion> findByreceptorOrderByIdentificadorDesc(String receptor);
	
}
