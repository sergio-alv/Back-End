package com.ebrozon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ebrozon.repository.ventaRepository;
import com.ebrozon.repository.ofertaRepository;
import com.ebrozon.repository.usuarioRepository;
import com.ebrozon.model.oferta;


import java.security.MessageDigest;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

@RestController
public class ofertaController {
	@Autowired
    ofertaRepository repository;
	
	@Autowired
    usuarioRepository repository_u;
	
	@Autowired
	ventaRepository repository_v;
	
	// Guarda una oferta recibiendo como parámetros obligatorios el nombre del usuario que la realiza,
	// el número de la venta, la cantidad, la fecha y el nombre del producto.
	@RequestMapping("/hacerOferta")
	public String hacerOferta(@RequestParam("usuario") String usuario, @RequestParam("nventa") int nventa, @RequestParam("cantidad") float cantidad, @RequestParam("fecha") Date fecha, @RequestParam("producto") String producto) {
		if (!repository.existsByusuarioAndcantidadAndproducto(usuario,cantidad,producto)) {
			if (!repository_v.existsByidentificador(nventa) || !repository_u.existsBynombreusuario(usuario)) {
				return "{E:No existe la venta o el usuario.}";
			}
			else {
				oferta o;
				int id = 1;
				try {
					o = new oferta(usuario,nventa,fecha,cantidad,(@NotNull short) 0,producto);
					Optional<Integer> idAux = repository.lastId();
					if(idAux.isPresent()) {
						id = idAux.get()+1;
					}
					o.setIndentificador(id);
				}
				catch(Exception e){
					return e.getMessage();
				}
				
				repository.save(o);
			}
		}
		return "{O:Ok}";
	}
	
	// Lista todas las ofertas recibidas sobre una venta recibiendo como parámetros obligatorios el número de la venta.
	@RequestMapping("/listarOfertasVenta")
	public List<oferta> listarOfertasVenta(@RequestParam("nventa") int nventa) {
		return repository.findBynventaOrderByFechaDesc(nventa);
	}
	
	// Lista todas las ofertas realizadas por un usuario recibiendo como parámetros obligatorios el nombre del usuario.
	@RequestMapping("/listarOfertasUsuario")
	public List<oferta> listarOfertasUsuario(@RequestParam("usuario") String usuario) {
		return repository.findByusuarioOrderByFechaDesc(usuario);
	}
	
	// Lista todas las ofertas sobre un producto recibiendo como parámetros obligatorios el nombre del producto.
	@RequestMapping("/listarOfertasProducto")
	public List<oferta> listarOfertasProducto(String producto) {
		return repository.findByproductoOrderByFechaDesc(producto);
	}
	
	// Acepta la oferta recibiendo como parámetros obligatorios el nombre del usuario que la realizó, el número de venta, la fecha y la cantidad.
	@RequestMapping("/aceptarOferta")
	public String aceptarOferta(@RequestParam("usuario") String usuario, @RequestParam("nventa") int nventa, @RequestParam("cantidad") float cantidad, @RequestParam("fecha") Date fecha) {
		if (!repository.existsByusuarioAndnventaAndfechaAndcantidad(usuario,nventa,fecha,cantidad)) {
			return "{E:No existe tal oferta}";
		}
		else {
			if (!repository_v.existsByidentificador(nventa) || !repository_u.existsBynombreusuario(usuario)) {
				return "{E:No existe la venta o el usuario.}";
			}
			else {
				oferta o;
				try {
					Optional<oferta> o_aux = repository.findByusuarioAndNventaAndFechaAndCantidad(usuario,nventa,fecha,cantidad);
					o = o_aux.get();
					o.setAceptada((short) 1);
				}
				catch (Exception e) {
					return e.getMessage();
				}
				repository.save(o);
				
				List<oferta> l = listarOfertasVenta(nventa);
				for (int i=0; i<l.size(); ++i) {
					if (l.get(i).getAceptada() == (short) 0) {
						try {
							l.get(i).setAceptada((short) -1);
						}
						catch (Exception e) {
							return e.getMessage();
						}
						repository.save(l.get(i));
					}
				}
				return "{O:Ok}";
			}
		}
	}
	
	// Rechaza la oferta recibiendo como parámetros obligatorios el nombre del usuario que la realizó, el número de venta, la fecha y la cantidad.
	@RequestMapping("/rechazarOferta")
	public String rechazarOferta(@RequestParam("usuario") String usuario, @RequestParam("nventa") int nventa, @RequestParam("cantidad") float cantidad, @RequestParam("fecha") Date fecha) {
		if (!repository.existsByusuarioAndnventaAndfechaAndcantidad(usuario,nventa,fecha,cantidad)) {
			return "{E:No existe tal oferta}";
		}
		else {
			if (!repository_v.existsByidentificador(nventa) || !repository_u.existsBynombreusuario(usuario)) {
				return "{E:No existe la venta o el usuario.}";
			}
			else {
				oferta o;
				try {
					Optional<oferta> o_aux = repository.findByusuarioAndNventaAndFechaAndCantidad(usuario,nventa,fecha,cantidad);
					o = o_aux.get();
					o.setAceptada((short) -1);
				}
				catch (Exception e) {
					return e.getMessage();
				}
				repository.save(o);
				return "{O:Ok}";
			}
		}
	}
	
	// Elimina la oferta recibiendo como parámetros obligatorios el nombre del usuario que la realizó, el número de venta, la fecha y la cantidad.
	@RequestMapping("/retirarOferta")
	public String retirarOferta(@RequestParam("usuario") String usuario, @RequestParam("nventa") int nventa, @RequestParam("cantidad") float cantidad, @RequestParam("fecha") Date fecha) {
		if (!repository.existsByusuarioAndnventaAndfechaAndcantidad(usuario,nventa,fecha,cantidad)) {
			return "{E:No existe tal oferta}";
		}
		else {
			if (!repository_v.existsByidentificador(nventa) || !repository_u.existsBynombreusuario(usuario)) {
				return "{E:No existe la venta o el usuario.}";
			}
			else {
				Optional<oferta> o_aux = repository.findByusuarioAndNventaAndFechaAndCantidad(usuario,nventa,fecha,cantidad);
				repository.delete(o_aux.get());
				return "{O:Ok}";
			}
		}
	}
}