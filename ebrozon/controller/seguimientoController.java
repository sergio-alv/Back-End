package com.ebrozon.controller;

import com.ebrozon.repository.ventaRepository;
import com.ebrozon.repository.seguimientoRepository;
import com.ebrozon.repository.usuarioRepository;
import com.ebrozon.model.seguimiento;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.security.MessageDigest;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;


//Funciones principales:
//-Seguir a un producto -> usuario, venta, hora /Si no me equioco -> significa dado
//-Listar productos seguidos (solo activos) recuperando tambien el nombre del producto
//-Dejar de seguir un producto
//-Obtener cantidad de seguidos que tiene una venta
@RestController
public class seguimientoController {
	@Autowired
    seguimientoRepository repository;
	
	@Autowired
    usuarioRepository repository_u;
	
	@Autowired
	ventaRepository repository_v;
	
	// Crea el seguimiento de un producto recibiendo como parámetros obligatorios el nombre del usuario que lo realiza,
	// el número de la venta, la fecha y el nombre del producto.
	@RequestMapping("/seguirProducto")
	public String seguirProducto(@RequestParam("usuario") String usuario, @RequestParam("nventa") int nventa, @RequestParam("fecha") Date fecha, @RequestParam("producto") String producto) {
		if (!repository.existsByusuarioAndnventaAndproducto(usuario,nventa,producto)) {
			if (!repository_v.existsByidentificador(nventa) || !repository_u.existsBynombreusuario(usuario)) {
				return "{E:No existe la venta o el usuario.}";
			}
			else {
				seguimiento s;
				try {
					s = new seguimiento(usuario,nventa,producto);
				}
				catch(Exception e){
					return e.getMessage();
				}
				
				repository.save(s);
			}
		}
		return "{O:Ok}";
	}
	
  // Lista todas los seguimientos sobre una venta recibiendo como parámetros obligatorios el número de la venta.
	@RequestMapping("/listarSeguimientosVenta")
	public List<seguimiento> listarSeguimientosVenta(@RequestParam("nventa") int nventa) {
		return repository.findBynventaOrderByFechaDesc(nventa);
	}
	
	// Lista todas los seguimientos realizadas por un usuario recibiendo como parámetros obligatorios el nombre del usuario.
	@RequestMapping("/listarSeguimientosUsuario")
	public List<oferta> listarSeguimientosUsuario(@RequestParam("usuario") String usuario) {
		return repository.findByusuarioOrderByFechaDesc(usuario);
	}
	
	// Lista todas los seguimientos sobre un producto recibiendo como parámetros obligatorios el nombre del producto.
	@RequestMapping("/listarSeguimientosProducto")
	public List<oferta> listarSeguimientosProducto(String producto) {
		return repository.findByproductoOrderByFechaDesc(producto);
	}
	
	// Elimina el seguimiento recibiendo como parámetros obligatorios el nombre del usuario que la realizó, el número de venta, la fecha y la cantidad.
	@RequestMapping("/eliminarSeguimiento")
	public String eliminarSeguimiento(@RequestParam("usuario") String usuario, @RequestParam("nventa") int nventa, @RequestParam("fecha") Date fecha) {
		if (!repository.existsByusuarioAndnventaAndfecha(usuario,nventa,fecha)) {
			return "{E:No existe tal seguimiento}";
		}
		else {
			if (!repository_v.existsByidentificador(nventa) || !repository_u.existsBynombreusuario(usuario)) {
				return "{E:No existe la venta o el usuario.}";
			}
			else {
				Optional<seguimiento> s_aux = repository.findByusuarioAndNventaAndFecha(usuario,nventa,fecha);
				repository.delete(s_aux.get());
				return "{O:Ok}";
			}
		}
	}
  
  //Obtiene la cantidad de seguidos que tiene una venta recibiendo como parámetros el número de venta
  @RequestMapping("/cantidadSeguidosVenta")
  public int cantidadSeguidosVenta(@RequestParam("nventa") int nventa){
    return listarSeguimientosVenta(nventa).size();  
  }
  
  //Obtiene la cantidad de seguidos que tiene un usuario recibiendo como parámetros el nombre de usuario
  @RequestMapping("/cantidadSeguidosUsuario")
  public int cantidadSeguidosVenta(@RequestParam("usuario") String usuario){
    return listarSeguimientosUsuario(usuario).size();  
  }
  
  //Obtiene la cantidad de seguidos que tiene un producto recibiendo como parámetros el nombre del producto
  @RequestMapping("/cantidadSeguidosProducto")
  public int cantidadSeguidosVenta(String producto){
    return listarSeguimientosVenta(producto).size();  
  }
}

