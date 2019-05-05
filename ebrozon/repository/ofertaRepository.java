package com.ebrozon.repository;
 
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ebrozon.model.oferta;

public interface ofertaRepository extends CrudRepository<oferta, Long>{
	boolean existsByusuarioAndcantidadAndproducto(String usuario, Float cantidad, String producto);
	
	List<oferta> findBynventaOrderByFechaDesc(int nventa);
	
	List<oferta> findByusuarioOrderByFechaDesc(String usuario);
	
	List<oferta> findByproductoOrderByFechaDesc(String producto);
	
	boolean existsByusuarioAndnventaAndfechaAndcantidad(String usuario, int nventa, Date fecha, Float cantidad);
	
	Optional<oferta> findByusuarioAndNventaAndFechaAndCantidad(String usuario, int nventa, Date fecha, Float cantidad);
	
	@Query("Select max(identificador) from venta")
	Optional<Integer> lastId();
}