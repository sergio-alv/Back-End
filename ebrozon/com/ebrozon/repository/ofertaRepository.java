package com.ebrozon.repository;
 
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

	List<oferta> findByusuarioAndAceptadaOrderByFechaDesc(String usuario, int i);

	List<oferta> findByproductoAndAceptadaOrderByFechaDesc(String producto, int i);

	List<oferta> findBynventaInAndAceptadaOrderByFechaDesc(List<Integer> nvs, int i);
	
	@Query(value = "Select * from oferta\n"
			+ "where usuario = :un and aceptada = 1\n"
			+ "and (select fechapago from venta\n"
			+ "		where identificador = nventa) is null\n", nativeQuery = true)
	List<oferta> ofertasRealizadasAceptadasPendientes(String un);
	
	@Query(value = "Select * from oferta\n"
			+ "where aceptada = 1 and nventa in :nvs\n"
			+ "and (select fechapago from venta\n"
			+ "		where identificador = nventa) is null\n", nativeQuery = true)
	List<oferta> ofertasRecibidasAceptadasPendientes(List<Integer> nvs);
	
	@Query(value = "Select * from oferta\n"
			+ "where aceptada = 0 and nventa in :nvs\n", nativeQuery = true)
	List<oferta> ofertasRecibidas(List<Integer> nvs);
	
	@Modifying
	@Transactional
	@Query(value = "update oferta set aceptada = 2 where nventa = :nv and aceptada = 1", nativeQuery = true)
	void confirmarPagoVenta(int nv);
	
	@Modifying
	@Transactional
	@Query(value = "update oferta set aceptada = -1 where nventa = :nv and aceptada = 1", nativeQuery = true)
	void cancelarPagoVenta(int nv);
}