package com.ebrozon.repository;
 
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ebrozon.model.seguimiento;

public interface seguimientoRepository extends CrudRepository<seguimiento, Long>{
	boolean existsByusuarioAndNventaAndProducto(String usuario, int nventa, String producto);
  
  List<seguimiento> findBynventaOrderByFechaDesc(int nventa);
	
	List<seguimiento> findByusuarioOrderByFechaDesc(String usuario);
	
	List<seguimiento> findByproductoOrderByFechaDesc(String producto);
    
	Optional<seguimiento> findByusuarioAndNventaAndProducto(String usuario, int nventa, String producto);
  
  boolean existsByusuarioAndNventaAndFecha(String usuario, int nventa, Date fecha);
	
	Optional<seguimiento> findByusuarioAndNventaAndFecha(String usuario, int nventa, Date fecha);
	
	@Query("Select max(identificador) from seguimiento")
	Optional<Integer> lastId();
}