package com.ebrozon.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ebrozon.model.venta;
import com.ebrozon.model.usuario;
import com.ebrozon.repository.ventaRepository;

@RestController
public class ventaController {
	
	@Autowired
    ventaRepository repository;
	
	@Autowired
    archivoController archiver;
	
	@Autowired
    usuarioController userer;
	
	@Autowired
    subastaController subaster;
	
	//Publica una venta recibiendo como parámetros nombre de usuario, título del producto, descripción
	//y precio, siendo opcionales los archivos
	@CrossOrigin
	@RequestMapping("/publicarVenta")
	public String publicarVenta(@RequestParam("un") String un, @RequestParam("prod") String prod, @RequestParam("desc") String desc,
			@RequestParam("pre") double pre, @RequestParam(value = "arc1") String arc1, @RequestParam(value = "arc2", required=false) String arc2
			, @RequestParam(value = "arc3", required=false) String arc3, @RequestParam(value = "arc4", required=false) String arc4) {
		Optional<usuario> usaux = userer.recuperarUsuario(un);
		if(!usaux.isPresent()) {
			return "{E:No existe el usuario.}";
		}
		int idIm = 1;
		int id = 1;
		boolean archivoGuardado = false;
		boolean ventaGuardada = false;
		venta vent;
		try {
			vent = new venta(un,prod, desc, pre, 1, 1, usaux.get().getCiudad());
			vent.setProvincia(usaux.get().getProvincia());
			usaux = null;
			Optional<Integer> idAux = repository.lastId();
			if(idAux.isPresent()) {
				id = idAux.get()+1;
			}
			vent.setIdentificador(id);
			if(arc1 != null) {
				vent.setTienearchivo(1);
				repository.save(vent);
				ventaGuardada = true;
				idIm = archiver.uploadArchivoTemp(arc1);
				if(idIm != -1) {archivoGuardado = true;}
				repository.archivoApareceEnVenta(idIm, id);
				if(arc2 != null) {
					idIm = archiver.uploadArchivoTemp(arc2);
					repository.archivoApareceEnVenta(idIm, id);
				}
				if(arc3 != null) {
					idIm = archiver.uploadArchivoTemp(arc3);
					repository.archivoApareceEnVenta(idIm, id);
				}
				if(arc4 != null) {
					idIm = archiver.uploadArchivoTemp(arc4);
					repository.archivoApareceEnVenta(idIm, id);
				}
				return "{O:Ok}";
			}
			else {
				return "{E:Es obligatorio insertar al menos una imagen del producto.}";
			}
		}
		catch(Exception e) {
			if(archivoGuardado) {repository.borrarArchivosVenta(id);}
			if(ventaGuardada) {repository.deleteByidentificador(id);}
			try {
				Throwable t = e.getCause();
				while ((t != null) && !(t instanceof ConstraintViolationException)) {
			        t = t.getCause();
			    }
				ConstraintViolationException aux = (ConstraintViolationException) t;
				Set<ConstraintViolation<?>> list = aux.getConstraintViolations();
				String error = "";
				for (ConstraintViolation<?> s : list) {
				    error = error + s.getMessage() + "\n";
				}
				return "{E:" + error + "}";
			}catch(Exception e2) {
				return "{E:Error no identificado.}";
			}
		}
	}
	
	@CrossOrigin
	@RequestMapping("/actualizarVenta")
	public String actualizarVenta(@RequestParam("id") int id, @RequestParam("prod") String prod, @RequestParam("desc") String desc,
			@RequestParam("pre") double pre, @RequestParam(value = "arc1") String arc1, @RequestParam(value = "arc2", required=false) String arc2
			, @RequestParam(value = "arc3", required=false) String arc3, @RequestParam(value = "arc4", required=false) String arc4) {
		Optional<venta> ventaux = repository.findByidentificador(id);
		if(!ventaux.isPresent()) {
			return "{E:Error inesperado.}";
		}
		int idIm = 1;
		boolean archivoGuardado = false;
		venta vent = ventaux.get();
		try {
			vent.setProducto(prod);
			vent.setDescripcion(desc);
			vent.setPrecio(pre);
			if(arc1 != null) {
				vent.setTienearchivo(1);
				repository.save(vent);
				idIm = archiver.uploadArchivoTemp(arc1);
				if(idIm != -1) {archivoGuardado = true;}
				repository.archivoApareceEnVenta(idIm, id);
				if(arc2 != null) {
					idIm = archiver.uploadArchivoTemp(arc2);
					repository.archivoApareceEnVenta(idIm, id);
				}
				if(arc3 != null) {
					idIm = archiver.uploadArchivoTemp(arc3);
					repository.archivoApareceEnVenta(idIm, id);
				}
				if(arc4 != null) {
					idIm = archiver.uploadArchivoTemp(arc4);
					repository.archivoApareceEnVenta(idIm, id);
				}
				return "{O:Ok}";
			}
			else {
				return "{E:Es obligatorio insertar al menos una imagen del producto.}";
			}
		}
		catch(Exception e) {
			if(archivoGuardado) {repository.borrarArchivosVenta(id);}
			try {
				Throwable t = e.getCause();
				while ((t != null) && !(t instanceof ConstraintViolationException)) {
			        t = t.getCause();
			    }
				ConstraintViolationException aux = (ConstraintViolationException) t;
				Set<ConstraintViolation<?>> list = aux.getConstraintViolations();
				String error = "";
				for (ConstraintViolation<?> s : list) {
				    error = error + s.getMessage() + "\n";
				}
				return "{E:" + error + "}";
			}catch(Exception e2) {
				return "{E:Error no identificado.}";
			}
		}
	}
	
	@CrossOrigin
	@RequestMapping("/listarPaginaPrincipal")
	@Produces("application/json")
	List<venta> listarPaginaPrincipal(@RequestParam("id") int id){
		return  repository.findFirst25ByactivaAndIdentificadorGreaterThanOrderByFechainicioDesc(1,id);
	}
	
	@CrossOrigin
	@RequestMapping("/listarProductosCiudad")
	@Produces("application/json")
	List<venta> listarProductosCiudad(@RequestParam("ci") String ci, @RequestParam("id") int id){
		return repository.findFirst25ByciudadAndActivaAndIdentificadorGreaterThanOrderByFechainicioDesc(ci,1,id);
	}
	
	@CrossOrigin
	@RequestMapping("/listarProductosProvincia")
	@Produces("application/json")
	List<venta> listarProductosProvincia(@RequestParam("pr") String pr, @RequestParam("id") int id){
		return repository.findFirst25ByprovinciaAndActivaAndIdentificadorGreaterThanOrderByFechainicioDesc(pr,1,id);
	}
	
	@CrossOrigin
	@RequestMapping("/listarProductosUsuario")
	@Produces("application/json")
	List<venta> listarProductosUsuario(@RequestParam("un") String un, @RequestParam("id") int id){
		return repository.findFirst25ByusuarioAndIdentificadorGreaterThanOrderByFechainicioDesc(un,id);
	}
	
	@CrossOrigin
	@RequestMapping("/recuperarProducto")
	@Produces("application/json")
	Optional<venta> recuperarProducto(@RequestParam("id") int id){
		Optional<venta> aux  = repository.findByidentificador(id);
		if(aux.isPresent()) {
			aux.get().setArchivos(repository.listaArchivos(id));
		}
		return aux;
	}
	
	String actualizarCiudadVentasUsuario(String un,String ci) {
		try {
			repository.updateCityVentasUsuario(un, ci);
		}
		catch(Exception e) {
			return "{E:Error inesperado.}";
		}
		return "{O:Ok}";
	}
	
	String actualizarProvinciaVentasUsuario(String un,String pr) {
		try {
			repository.updateProvinciaVentasUsuario(un, pr);
		}
		catch(Exception e) {
			return "{E:Error inesperado.}";
		}
		return "{O:Ok}";
	}
	
	@CrossOrigin
	@RequestMapping("/desactivarVenta")
	String desactivarVenta(@RequestParam("id") int id) {
		Optional<venta> aux = repository.findByidentificador(id);
		if(aux.isPresent()) {
			venta vent = aux.get();
			vent.setActiva(0);
			repository.save(vent);
			return "{O:Ok}";
		}
		else {
			return "{E:No se ha encontrado la venta.}";
		}
	}
	
	@CrossOrigin
	@RequestMapping("/activarVenta")
	String activarVenta(@RequestParam("id") int id) {
		Optional<venta> aux = repository.findByidentificador(id);
		if(aux.isPresent()) {
			venta vent = aux.get();
			vent.setActiva(1);
			repository.save(vent);
			return "{O:Ok}";
		}
		else {
			return "{E:No se ha encontrado la venta.}";
		}
	}
}
