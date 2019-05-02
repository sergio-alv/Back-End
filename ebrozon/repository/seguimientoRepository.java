package com.ebrozon.repository;
 
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ebrozon.model.seguimiento;

public interface seguimientoRepository extends CrudRepository<seguimiento, Long>{
	boolean existsByusuarioAndnventaAndproducto(String usuario, int nventa, String producto);
  
  List<seguimiento> findBynventaOrderByFechaDesc(int nventa);
	
	List<seguimiento> findByusuarioOrderByFechaDesc(String usuario);
	
	List<seguimiento> findByproductoOrderByFechaDesc(String producto);
    
	Optional<seguimiento> findByusuarioAndNventaAndproducto(String usuario, int nventa, String producto);
  
  boolean existsByusuarioAndnventaAndfecha(String usuario, int nventa, Date fecha);
	
	Optional<seguimiento> findByusuarioAndNventaAndFecha(String usuario, int nventa, Date fecha);
}