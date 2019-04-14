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
	
	List<venta> findByciudadAndActiva(String ciudad,int Activa);
	
	List<venta> findByusuarioAndActiva(String usuario, int Activa);
	
	Optional<venta> findByidentificador(int identificador);
	
	Optional<venta> findByusuario(String usuario);
	
	@Query("Select max(identificador) from venta")
	Optional<Integer> lastId();
	
	@Modifying
	@Transactional
	@Query(value = "update venta set ciudad = :ci where usuario = :un", nativeQuery = true)
	void updateCityVentasUsuario(String un, String ci);
}