package com.ebrozon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebrozon.repository.ventaRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
@Api(value="Offer Management System", description="Operations pertaining to offer in Offer Managament System ")
public class ofertaController {
	@Autowired
    ofertaRepository repository;
	
	@Autowired
    usuarioRepository repository_u;
	
	@Autowired
	ventaRepository repository_v;
	
	@Autowired
	ventaController venter;
	
	// Guarda una oferta recibiendo como parámetros obligatorios el nombre del usuario que la realiza,
	// el número de la venta, la cantidad, la fecha y el nombre del producto.
	@ApiOperation(value = "Make an offer to a sale", response = String.class)
	@CrossOrigin
	@RequestMapping("/hacerOferta")
	public String hacerOferta(@RequestParam("un") String usuario, @RequestParam("nv") int nventa, @RequestParam("can") float cantidad) {
		if (!repository.existsByusuarioAndNventa(usuario,nventa) || repository_v.findByidentificador(nventa).get().getPrecio() <= cantidad) {
			if (!repository_v.existsByidentificador(nventa) || !repository_u.existsBynombreusuario(usuario)) {
				return "{E:No existe la venta o el usuario.}";
			}
			else {
				oferta o;
				int id = 1;
				try {
					venta v = repository_v.findByidentificador(nventa).get();
					o = new oferta(usuario,nventa,cantidad,(@NotNull short) 0,v.getProducto());
					o.setVendedor(v.getUsuario());
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
								
						o.setAceptada(1);
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
			aux.setAceptada(0);
			repository.save(aux);
		}
		return "{O:Ok}";
	}
	
	// Lista todas las ofertas recibidas sobre una venta recibiendo como parámetros obligatorios el número de la venta.
	@ApiOperation(value = "List all offers recieved on a sale", response = List.class)
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarOfertasVenta")
	public List<oferta> listarOfertasVenta(@RequestParam("nv") int nventa) {
		return repository.findBynventaOrderByFechaDesc(nventa);
	}
	
	// Lista todas las ofertas realizadas por un usuario recibiendo como parámetros obligatorios el nombre del usuario.
	@ApiOperation(value = "List all offers made by a user", response = List.class)
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarOfertasRealizadas")
	public List<oferta> listarOfertasRealizadas(@RequestParam("un") String usuario) {
		return repository.findByusuarioAndAceptadaOrderByFechaDesc(usuario,0);
	}
	
	@ApiOperation(value = "List all offers made, accepted and pending by a user", response = List.class)
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarOfertasRealizadasAceptadasPendientes")
	public List<oferta> listarOfertasRealizadasAceptadasPendientes(@RequestParam("un") String usuario) {
		return repository.ofertasRealizadasAceptadasPendientes(usuario);
	}
	
	@ApiOperation(value = "List all offers recieved, accepted and pending by a user", response = List.class)
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarOfertasRecibidasAceptadasPendientes")
	public List<oferta> listarOfertasRecibidasAceptadasPendientes(@RequestParam("un") String usuario) {
		List<Integer> nvs = repository_v.numerosVentasUsuario(usuario);
		return repository.ofertasRecibidasAceptadasPendientes(nvs);
	}
	
	@ApiOperation(value = "List all offers recieved by a user", response = List.class)
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarOfertasRecibidas")
	public List<oferta> listarOfertasRecibidas(@RequestParam("un") String usuario) {
		List<Integer> nvs = repository_v.numerosVentasUsuario(usuario);
		return repository.ofertasRecibidas(nvs);
	}
	
	// Lista todas las ofertas sobre un producto recibiendo como parámetros obligatorios el nombre del producto.
	@ApiOperation(value = "List all offers of a product", response = List.class)
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarOfertasProducto")
	public List<oferta> listarOfertasProducto(String producto) {
		return repository.findByproductoAndAceptadaOrderByFechaDesc(producto,0);
	}
	
	// Acepta la oferta recibiendo como parámetros obligatorios el nombre del usuario que la realizó, el número de venta, la fecha y la cantidad.
	@ApiOperation(value = "Accept an offer", response = String.class)
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
	@ApiOperation(value = "Reject an offer", response = String.class)
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
	@ApiOperation(value = "Remove an offer", response = String.class)
	@CrossOrigin
	@RequestMapping("/retirarOferta")
	public String retirarOferta(@RequestParam("id") int id) {
		if (!repository.existsByIdentificador(id)) {
			return "{E:No existe tal oferta}";
		}
		else {
			Optional<oferta> o_aux = repository.findByIdentificador(id);
			if(o_aux.get().getAceptada() == 1) {
				venter.cancelarPagoVenta(o_aux.get().getIdentificador());
			}
			repository.delete(o_aux.get());
			return "{O:Ok}";
		}
	}
}