package com.ebrozon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebrozon.repository.ventaRepository;
import com.ebrozon.repository.ofertaRepository;
import com.ebrozon.repository.usuarioRepository;
import com.ebrozon.model.oferta;
import com.ebrozon.model.venta;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Produces;

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
	@CrossOrigin
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
					if(v.getes_subasta() == 1 && cantidad < v.getPrecio()) {
						return "{E:Un producto en subasta solo puede recibir ofertas mayores o iguales al precio de compra inmediata.}";
					}
					else if(cantidad >= v.getPrecio()) {
						v.setActiva(0);
						v.setFechaventa(new Date());
						v.setComprador(usuario);
						v.setPreciofinal(v.getPrecio());
						repository_v.save(v);
						return "{O:Ok}";
					}
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
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarOfertasVenta")
	public List<oferta> listarOfertasVenta(@RequestParam("nv") int nventa) {
		return repository.findBynventaOrderByFechaDesc(nventa);
	}
	
	// Lista todas las ofertas realizadas por un usuario recibiendo como parámetros obligatorios el nombre del usuario.
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarOfertasRealizadas")
	public List<oferta> listarOfertasRealizadas(@RequestParam("un") String usuario) {
		return repository.findByusuarioAndAceptadaOrderByFechaDesc(usuario,0);
	}
	
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarOfertasRealizadasAceptadasPendientes")
	public List<oferta> listarOfertasRealizadasAceptadasPendientes(@RequestParam("un") String usuario) {
		return repository.ofertasRealizadasAceptadasPendientes(usuario);
	}
	
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarOfertasRecibidasAceptadasPendientes")
	public List<oferta> listarOfertasRecibidasAceptadasPendientes(@RequestParam("un") String usuario) {
		List<Integer> nvs = repository_v.numerosVentasUsuario(usuario);
		return repository.ofertasRecibidasAceptadasPendientes(nvs);
	}
	
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarOfertasRecibidas")
	public List<oferta> listarOfertasRecibidas(@RequestParam("un") String usuario) {
		List<Integer> nvs = repository_v.numerosVentasUsuario(usuario);
		return repository.ofertasRecibidas(nvs);
	}
	
	// Lista todas las ofertas sobre un producto recibiendo como parámetros obligatorios el nombre del producto.
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarOfertasProducto")
	public List<oferta> listarOfertasProducto(String producto) {
		return repository.findByproductoAndAceptadaOrderByFechaDesc(producto,0);
	}
	
	// Acepta la oferta recibiendo como parámetros obligatorios el nombre del usuario que la realizó, el número de venta, la fecha y la cantidad.
	@CrossOrigin
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
			try {
				venta aux = repository_v.findByidentificador(o.getNventa()).get();
				aux.setActiva(0);
				aux.setFechaventa(new Date());
				aux.setComprador(o.getUsuario());
				aux.setPreciofinal(o.getCantidad());
				repository_v.save(aux);
			}
			catch(Exception e){
				return "{E:Ha habido un error inesperado.}";
			}
			return "{O:Ok}";
		}
	}
	
	// Rechaza la oferta recibiendo como parámetros obligatorios el nombre del usuario que la realizó, el número de venta, la fecha y la cantidad.
	@CrossOrigin
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
	@CrossOrigin
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