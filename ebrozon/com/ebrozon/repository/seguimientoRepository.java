package com.ebrozon.repository;
 
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ebrozon.model.seguimiento;

public interface seguimientoRepository extends CrudRepository<seguimiento, Long>{
	boolean existsByusuarioAndNventa(String usuario, int nventa);
    
    	boolean existsByidentificador(int id);
    
    	Optional<seguimiento> findByidentificador(int id);
  
  	List<seguimiento> findBynventaOrderByFechaDesc(int nventa);
	
	List<seguimiento> findByusuarioOrderByFechaDesc(String usuario);
	
	List<seguimiento> findByproductoOrderByFechaDesc(String producto);
    
	Optional<seguimiento> findByusuarioAndNventaAndProducto(String usuario, int nventa, String producto);
	
	@Query("Select max(identificador) from seguimiento")
	Optional<Integer> lastId();
	
	@Query(value = "Select aux.identificador\r\n" + 
			"from (Select identificador, (select count(*) from seguimiento where nventa = v.identificador) as c\r\n" + 
			"	from venta v) as aux\r\n" + 
			"order by c desc, identificador desc", nativeQuery = true)
	List<Integer> ventasPorSeguimientos();
}
