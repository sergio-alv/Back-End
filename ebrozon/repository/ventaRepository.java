package com.ebrozon.repository;
 
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
	@Query(value = "insert into archivosventa(archivo, nventa) values(:arc, :nv)", nativeQuery = true)
	void archivoApareceEnVenta(int arc, int nv);
	
	List<venta> findByciudadAndActivaOrderByFechainicioDesc(String ciudad,int Activa);
	
	List<venta> findByusuarioAndActivaOrderByFechainicioDesc(String usuario, int Activa);
	
	List<venta> findByactivaOrderByFechainicioDesc(int activa);
	
	Optional<venta> findByidentificador(int identificador);
	
	List<venta> findByusuarioOrderByFechainicioDesc(String usuario);
	
	@Query("Select max(identificador) from venta")
	Optional<Integer> lastId();
	
	@Modifying
	@Transactional
	@Query(value = "update venta set ciudad = :ci where usuario = :un", nativeQuery = true)
	void updateCityVentasUsuario(String un, String ci);
}