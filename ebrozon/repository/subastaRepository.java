package com.ebrozon.repository;
 
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ebrozon.model.subasta;

public interface subastaRepository extends CrudRepository<subasta, Long>{
	
	@Modifying
	@Transactional
	@Query(value = "insert into archivosventa(archivo, nventa) values(:arc, :nv)", nativeQuery = true)
	void archivoApareceEnVenta(int arc, int nv);
	
	@Modifying
	@Transactional
	@Query(value = "delete from archivosventa where nventa = :nv", nativeQuery = true)
	void borrarArchivosVenta(int nv);
	
	List<subasta> findByciudadAndActivaOrderByFechainicioDesc(String ciudad,int Activa);
	
	List<subasta> findByusuarioAndActivaOrderByFechainicioDesc(String usuario, int Activa);
	
	Optional<subasta> findByidentificador(int identificador);
	
	List<subasta> findByactiva(int ac);
	
	List<subasta> findByusuarioOrderByFechainicioDesc(String usuario);
	
	@Query("Select max(identificador) from venta")
	Optional<Integer> lastId();
	
	@Query(value = "Select count(usuario) from puja where nventa = :nv", nativeQuery = true)
	Integer numeroPujasRecibidas(int nv);
	
	@Query(value = "Select max(cantidad) from puja where nventa = :nv", nativeQuery = true)
	Optional<Double> pujaMaxima(int nv);
	
	@Modifying
	@Transactional
	@Query(value = "insert into puja(usuario, nventa, cantidad) values(:us, :nv, :ct)", nativeQuery = true)
	void pujarSubasta(String us, int nv, double ct);
	
	@Modifying
	@Transactional
	@Query(value = "update subasta set ciudad = :ci where usuario = :un", nativeQuery = true)
	void updateCitysubastasUsuario(String un, String ci);

	void deleteByidentificador(int id);

	List<subasta> findByprovinciaAndActivaOrderByFechainicioDesc(String pr, int i);

	List<subasta> findFirst25ByciudadAndActivaAndIdentificadorGreaterThanOrderByFechainicioDesc(String ci, int i,
			int id);

	List<subasta> findFirst25ByprovinciaAndActivaAndIdentificadorGreaterThanOrderByFechainicioDesc(String pr, int i,
			int id);
}