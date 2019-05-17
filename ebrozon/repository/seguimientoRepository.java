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
	
	@Query(value = "Select nvc.nv as venta\r\n" + 
			"	from (Select nventa as nv\r\n" + 
			"			from seguimiento) as nvc \r\n" + 
			"	group by nvc.nv \r\n" + 
			"	order by count(*) desc", nativeQuery = true)
	List<Integer> ventasPorSeguimientos();
}
