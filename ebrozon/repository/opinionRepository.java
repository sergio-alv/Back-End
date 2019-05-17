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

	@Query("Select count(*) from opinion where receptor = :un")
	int numeroOpinionesRecibidas(String un);

	@Query("Select count(*) from opinion where emisor = :un")
	int numeroOpinionesRealizadas(String un);
	
	@Query(value = "Select identificador\r\n" + 
			"from venta, ((Select nombreusuario as nun, estrellas as val, co\r\n" + 
			"				from usuario, (Select receptor as un, count(*) as co\r\n" + 
			"								from opinion\r\n" + 
			"								group by receptor) as aux1\r\n" + 
			"				where nombreusuario = aux1.un)\r\n" + 
			"				Union\r\n" + 
			"				(Select nombreusuario as nun, estrellas as val, 0 as co\r\n" + 
			"				from usuario\r\n" + 
			"				where 0 =(Select count(*)\r\n" + 
			"							from opinion\r\n" + 
			"							 where receptor = nombreusuario))) as aux2\r\n" + 
			"where usuario = aux2.nun\r\n" + 
			"order by aux2.val desc, aux2.co desc, identificador desc", nativeQuery = true)
	List<Integer> ventasPorValoraciones();
}
