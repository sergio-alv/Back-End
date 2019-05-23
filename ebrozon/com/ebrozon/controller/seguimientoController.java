package com.ebrozon.controller;

import com.ebrozon.repository.ventaRepository;
import com.ebrozon.repository.seguimientoRepository;
import com.ebrozon.repository.usuarioRepository;
import com.ebrozon.model.seguimiento;
import com.ebrozon.model.venta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.Produces;


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
	
	// Crea el seguimiento de un producto recibiendo como parametros obligatorios el nombre del usuario que lo realiza,
	// el numero de la venta, la fecha y el nombre del producto.
	@CrossOrigin
	@RequestMapping("/seguirProducto")
	public String seguirProducto(@RequestParam("un") String usuario, @RequestParam("nv") int nventa) {
    		if (!repository.existsByusuarioAndNventa(usuario,nventa)) {
				if (!repository_v.existsByidentificador(nventa) || !repository_u.existsBynombreusuario(usuario)) {
					return "{E:No existe la venta o el usuario.}";
				}
				else {
					Optional<venta> aux =  repository_v.findByidentificador(nventa);
	        		String producto = "";
	        		producto = aux.get().getProducto();
					seguimiento s;
					try {
						s = new seguimiento(usuario,nventa,producto);
						int id = 1;
						Optional<Integer> idAux = repository.lastId();
						if(idAux.isPresent()) {
							id = idAux.get()+1;
						}
						s.setIdentificador(id);
						repository.save(s);
					}
					catch(Exception e){
						return "{E:Ha habido un problema inesperado.}";
					}
				}
    		}
    		return "{O:Ok}";
	}
	
  	// Lista todas los seguimientos sobre una venta recibiendo como parametros obligatorios el numero de la venta.
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarSeguimientosVenta")
	public List<seguimiento> listarSeguimientosVenta(@RequestParam("nv") int nventa) {
		return repository.findBynventaOrderByFechaDesc(nventa);
	}
	
	// Lista todas los seguimientos realizadas por un usuario recibiendo como parametros obligatorios el nombre del usuario.
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarSeguimientosUsuario")
	public List<seguimiento> listarSeguimientosUsuario(@RequestParam("un") String usuario) {
		return repository.findByusuarioOrderByFechaDesc(usuario);
	}
	
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarVentasSeguidasUsuario")
	public List<venta> listarVentasSeguidasUsuario(@RequestParam("un") String usuario, @RequestParam("id") int id) {
		List<seguimiento> aux = repository.findByusuarioOrderByFechaDesc(usuario);
		List<venta> list = new ArrayList<venta>();
		int count = 0;
		for(int i = 0; count < 25 && i < aux.size(); ++i) {
			if(aux.get(i).getNventa() < id) {
				list.add(repository_v.findByidentificador(aux.get(i).getNventa()).get());
				++count;
			}
		}
		return list;
	}
	
	// Elimina el seguimiento recibiendo como parametros obligatorios el nombre del usuario que la realizo, el numero de venta, la fecha y la cantidad.
	@CrossOrigin
	@RequestMapping("/eliminarSeguimiento")
	public String eliminarSeguimiento(@RequestParam("id") int id) {
		if (!repository.existsByidentificador(id)) {
			return "{E:No existe tal seguimiento}";
		}
		else {
			Optional<seguimiento> s_aux = repository.findByidentificador(id);
			repository.delete(s_aux.get());
			return "{O:Ok}";
		}
	}
	
	@CrossOrigin
	@RequestMapping("/dejarSeguirProducto")
	public String dejarSeguirProducto(@RequestParam("un") String un, @RequestParam("nv") int nv) {
		if (!repository.existsByusuarioAndNventa(un,nv)) {
			return "{E:No existe tal seguimiento}";
		}
		else {
			Optional<seguimiento> s_aux = repository.findByusuarioAndNventa(un,nv);
			repository.delete(s_aux.get());
			return "{O:Ok}";
		}
	}
  
  	//Obtiene la cantidad de seguidos que tiene una venta recibiendo como parametros el numero de venta
	@CrossOrigin
  	@RequestMapping("/cantidadSeguidosVenta")
  	public int cantidadSeguidosVenta(@RequestParam("nv") int nventa){
    		return listarSeguimientosVenta(nventa).size();  
  	}
  
  	//Obtiene la cantidad de seguidos que tiene un usuario recibiendo como parametros el nombre de usuario
	@CrossOrigin
  	@RequestMapping("/cantidadSeguidosUsuario")
  	public int cantidadSeguidosUsuario(@RequestParam("un") String usuario){
    		return listarSeguimientosUsuario(usuario).size();  
  	}
}



