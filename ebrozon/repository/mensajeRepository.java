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
	
	@Query("Select emisor from mensaje where receptor = :un group by emisor order by max(identificador) desc")
	List<String> usuariosChateados(String un);
	
	List<mensaje> findByemisorAndReceptorOrReceptorAndEmisorOrderByIdentificadorAsc(String un11, String un21, String un12, String un22);
	
	List<mensaje> findByidentificadorGreaterThanAndEmisorAndReceptorOrderByIdentificadorAsc(int identificador, String un11, String un21);
	
	@Query(value = "Select * \n"
			+ "from mensaje\n"
			+ "where identificador = "
			+ "(Select max(identificador) as maxid\n"
			+ "from mensaje\n"
			+ "where (receptor = :un1 and emisor = :un2)\n"
			+ " or (receptor = :un2 and emisor = :un1))\n", nativeQuery = true)
	Optional<mensaje> ultimoMensaje(String un1, String un2);
}
