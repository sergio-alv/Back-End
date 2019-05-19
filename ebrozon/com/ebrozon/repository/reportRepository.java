package com.ebrozon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ebrozon.model.report;

public interface reportRepository  extends CrudRepository<report, Long>{
	
	boolean existsByidentificador(int identificador);
	
	Optional<report> findByidentificador(int identificador);
	
	@Query("Select max(identificador) from report")
	Optional<Integer> lastId();
	
	List<report> findByemisorOrderByIdentificadorDesc(String emisor);
	
	List<report> findByreceptorOrderByIdentificadorDesc(String receptor);

	@Query("Select count(*) from report where receptor = :un")
	int numeroReportesRecibidos(String un);
	
}
