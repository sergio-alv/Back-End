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
	
	@Query(value = "Select aux2.u\r\n" +
			"from (Select aux.un as u, max(aux.mid) as m\r\n" + 
			"from ((Select emisor as un, max(identificador) as mid\r\n" + 
			"		from mensaje \r\n" + 
			"		where receptor = :un\r\n" + 
			"		group by emisor)\r\n" + 
			"		union\r\n" + 
			"		(Select receptor as un, max(identificador) as mid\r\n" + 
			"		from mensaje \r\n" + 
			"		where emisor = :un\r\n" + 
			"		group by receptor)) as aux\r\n" + 
			"group by u) as aux2 \r\n" +
			"order by aux2.m desc", nativeQuery = true)
	List<String> usuariosChateados(String un);
	
	List<mensaje> findByemisorAndReceptorOrReceptorAndEmisorOrderByIdentificadorAsc(String un11, String un21, String un12, String un22);
	
	List<mensaje> findByidentificadorGreaterThanAndEmisorAndReceptorOrIdentificadorGreaterThanAndEmisorAndReceptorOrderByIdentificadorAsc(int id1, String un11, String un21,int id2, String un12, String un22);
	
	@Query(value = "Select * \n"
			+ "from mensaje\n"
			+ "where identificador = "
			+ "(Select max(identificador) as maxid\n"
			+ "from mensaje\n"
			+ "where (receptor = :un1 and emisor = :un2)\n"
			+ " or (receptor = :un2 and emisor = :un1))\n", nativeQuery = true)
	Optional<mensaje> ultimoMensaje(String un1, String un2);
}
