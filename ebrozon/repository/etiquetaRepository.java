package com.ebrozon.repository;
 
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ebrozon.model.etiqueta;

public interface etiquetaRepository extends CrudRepository<etiqueta, Long>{
	boolean existsBynombre(String nombreetiqueta);
	
	Optional<etiqueta> findBynombre(String nombre);
	
	@Modifying
	@Transactional
	@Query(value = "insert into etiquetaventa(nventa, etiqueta) values(:nv, :et)", nativeQuery = true)
	void etiquetaApareceEnVenta(int nv, String et);
	
	@Query(value = "Select etiqueta from etiquetaventa where nventa = :nv", nativeQuery = true)
	List<String> etiquetasVenta(int nv);
	
	@Modifying
	@Transactional
	@Query(value = "delete from etiquetaventa where nventa = :nv", nativeQuery = true)
	void borrarEtiquetasVenta(int nv);
	
	@Query(value = "select distinct(nventa) from etiquetaventa where etiqueta in :et", nativeQuery = true)
	List<Integer> ventasConEtiquetas(String[] et);
	
	@Query(value = "select distinct(ev1.nventa) from etiquetaventa ev1 where ev1.etiqueta in :et and"
			+ " 2 <= (select count(*) from etiquetaventa ev2 where ev2.etiqueta in :et and ev1.nventa = ev2.nventa)", nativeQuery = true)
	List<Integer> ventasConEtiquetasMinDos(String[] et);
	
	@Query(value = "select aux.venta\n" + 
			"from (Select nvc.nv as venta, count(*) as c\n" + 
			" from (Select nventa as nv\n" + 
			" from etiquetaventa\n" + 
			" where etiqueta in :et) as nvc\n" + 
			" group by nvc.nv\n" + 
			" order by c desc) as aux\n", nativeQuery = true)
	List<Integer> ventasConEtiquetasOrdenadasPorCoincidencias(String[] et);
}