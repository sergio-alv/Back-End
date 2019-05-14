package com.ebrozon.repository;
 
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ebrozon.model.oferta;

public interface ofertaRepository extends CrudRepository<oferta, Long>{
	boolean existsByusuarioAndNventa(String usuario, int venta);
	
	List<oferta> findBynventaOrderByFechaDesc(int nventa);
	
	List<oferta> findByusuarioOrderByFechaDesc(String usuario);
	
	List<oferta> findByproductoOrderByFechaDesc(String producto);
	
	boolean existsByIdentificador(int id);
	
	Optional<oferta> findByusuarioAndNventa(String usuario, int nventa);
	
	Optional<oferta> findByIdentificador(int id);
	
	@Query("Select max(identificador) from oferta")
	Optional<Integer> lastId();
	
	@Modifying
	@Transactional
	@Query(value = "update oferta set producto = :prod where nventa = :nv", nativeQuery = true)
	void actualizarProductoOfertas(int nv, String prod);
}