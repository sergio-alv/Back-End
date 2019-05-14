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
import com.ebrozon.model.venta;

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
	public String hacerOferta(@RequestParam("un") String usuario, @RequestParam("nv") int nventa, @RequestParam("can") float cantidad) {
		if (!repository.existsByusuarioAndNventa(usuario,nventa)) {
			if (!repository_v.existsByidentificador(nventa) || !repository_u.existsBynombreusuario(usuario)) {
				return "{E:No existe la venta o el usuario.}";
			}
			else {
				oferta o;
				int id = 1;
				try {
					venta v = repository_v.findByidentificador(nventa).get();
					o = new oferta(usuario,nventa,cantidad,(@NotNull short) 0,v.getProducto());
					Optional<Integer> idAux = repository.lastId();
					if(idAux.isPresent()) {
						id = idAux.get()+1;
					}
					o.setIndentificador(id);
					repository.save(o);
				}
				catch(Exception e){
					return "{E:Ha habido un error inesperado.}";
				}
			}
		}
		else {
			oferta aux = repository.findByusuarioAndNventa(usuario, nventa).get();
			aux.setCantidad(cantidad);
			repository.save(aux);
		}
		return "{O:Ok}";
	}
	
	// Lista todas las ofertas recibidas sobre una venta recibiendo como parámetros obligatorios el número de la venta.
	@RequestMapping("/listarOfertasVenta")
	public List<oferta> listarOfertasVenta(@RequestParam("nv") int nventa) {
		return repository.findBynventaOrderByFechaDesc(nventa);
	}
	
	// Lista todas las ofertas realizadas por un usuario recibiendo como parámetros obligatorios el nombre del usuario.
	@RequestMapping("/listarOfertasUsuario")
	public List<oferta> listarOfertasUsuario(@RequestParam("un") String usuario) {
		return repository.findByusuarioOrderByFechaDesc(usuario);
	}
	
	// Lista todas las ofertas sobre un producto recibiendo como parámetros obligatorios el nombre del producto.
	@RequestMapping("/listarOfertasProducto")
	public List<oferta> listarOfertasProducto(String producto) {
		return repository.findByproductoOrderByFechaDesc(producto);
	}
	
	// Acepta la oferta recibiendo como parámetros obligatorios el nombre del usuario que la realizó, el número de venta, la fecha y la cantidad.
	@RequestMapping("/aceptarOferta")
	public String aceptarOferta(@RequestParam("id") int id) {
		if (!repository.existsByIdentificador(id)) {
			return "{E:No existe tal oferta}";
		}
		else {
			oferta o;
			try {
				Optional<oferta> o_aux = repository.findByIdentificador(id);
				o = o_aux.get();
				o.setAceptada((short) 1);
				repository.save(o);
			}
			catch (Exception e) {
				return "{E:Ha habido un error inesperado.}";
			}
			List<oferta> l = listarOfertasVenta(o.getNventa());
			for (int i=0; i<l.size(); ++i) {
				if (l.get(i).getAceptada() == (short) 0) {
					try {
						l.get(i).setAceptada((short) -1);
						repository.save(l.get(i));
					}
					catch (Exception e) {
						return e.getMessage();
					}
				}
			}
			return "{O:Ok}";
		}
	}
	
	// Rechaza la oferta recibiendo como parámetros obligatorios el nombre del usuario que la realizó, el número de venta, la fecha y la cantidad.
	@RequestMapping("/rechazarOferta")
	public String rechazarOferta(@RequestParam("id") int id) {
		if (!repository.existsByIdentificador(id)) {
			return "{E:No existe tal oferta}";
		}
		else {
			oferta o;
			try {
				Optional<oferta> o_aux = repository.findByIdentificador(id);
				o = o_aux.get();
				o.setAceptada((short) -1);
				repository.save(o);
			}
			catch (Exception e) {
				return e.getMessage();
			}
			return "{O:Ok}";
		}
	}
	
	// Elimina la oferta recibiendo como parámetros obligatorios el nombre del usuario que la realizó, el número de venta, la fecha y la cantidad.
	@RequestMapping("/retirarOferta")
	public String retirarOferta(@RequestParam("id") int id) {
		if (!repository.existsByIdentificador(id)) {
			return "{E:No existe tal oferta}";
		}
		else {
			Optional<oferta> o_aux = repository.findByIdentificador(id);
			repository.delete(o_aux.get());
			return "{O:Ok}";
		}
	}
}