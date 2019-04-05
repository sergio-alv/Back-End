package com.ebrozon.repository;
 
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ebrozon.model.venta;

public interface ventaRepository extends CrudRepository<venta, Long>{
	
	@Modifying
	@Transactional
	@Query(value = "insert into archivosventa(archivo, usuarioventa, fechaventa) values(:arc, :un, :fi)", nativeQuery = true)
	void archivoApareceEnVenta(int arc, String un, Date fi);
}