package com.ebrozon.controller;

import java.util.Date;
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

import com.ebrozon.model.subasta;
import com.ebrozon.model.usuario;
import com.ebrozon.repository.subastaRepository;

@RestController
public class subastaController {
	
	@Autowired
    subastaRepository repository;
	
	@Autowired
    archivoController archiver;
	
	@Autowired
    usuarioController userer;
	
	//Publica una venta recibiendo como parámetros nombre de usuario, título del producto, descripción
	//y precio, siendo opcionales los archivos
	@CrossOrigin
	@RequestMapping("/publicarSubasta")
	public String publicarSubasta(@RequestParam("un") String un, @RequestParam("prod") String prod, @RequestParam("desc") String desc,
			@RequestParam("pre") double pre, @RequestParam("end") /*Date*/String end,  @RequestParam("pin") double pin, 
			@RequestParam(value = "arc", required=false) MultipartFile arc) {
		Optional<usuario> usaux = userer.recuperarUsuario(un);
		if(!usaux.isPresent()) {
			return "{E:No existe el usuario.}";
		}
		int idIm = 1;
		boolean archivoGuardado = false;
		subasta sub;
		try {
			sub = new subasta(un,prod, desc, pre, 1, 1, usaux.get().getCiudad(),new Date(),pin,pin);
			int id = 1;
			Optional<Integer> idAux = repository.lastId();
			if(idAux.isPresent()) {
				id = idAux.get()+1;
			}
			sub.setIdentificador(id);
			/*if(arc != null) {
				vent.setTienearchivo(1);
				idIm = archiver.uploadFile(arc);
				if(idIm != -1) {archivoGuardado = true;}
				repository. archivoApareceEnVenta(idIm, un, vent.getFechainicio());
				repository.save(vent);
				return "{O:Ok}";
			}
			else {*/
				usaux = null;
				repository.save(sub);
				return "{O:Ok}";
				//return "{E:Es obligatorio insertar al menos una imagen del producto.}";
			//}
		}
		catch(Exception e) {
			if(archivoGuardado) {archiver.deleteFile(idIm);}
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
	@RequestMapping("/actualizarSubasta")
	public String actualizarSubasta(@RequestParam("id") int id, @RequestParam("prod") String prod, @RequestParam("desc") String desc,
			@RequestParam("pre") double pre, @RequestParam("end") /*Date*/String end,  @RequestParam("pin") double pin, 
			@RequestParam(value = "arc", required=false) MultipartFile arc) {
		
		if(repository.numeroPujasRecibidas(id) >0) {
			return "{E:No se puede actualizar una subasta cuando esta ha recibido ya una puja.}";
		}
		
		Optional<subasta> subaux = repository.findByidentificador(id);
		if(!subaux.isPresent()) {
			return "{E:Error inesperado.}";
		}
		int idIm = 1;
		boolean archivoGuardado = false;
		subasta sub = subaux.get();
		try {
			sub.setProducto(prod);
			sub.setDescripcion(desc);
			sub.setPrecio(pre);
			sub.setFechafin(new Date());
			sub.setPrecioinicial(pin);
			sub.setPujaactual(pin);
			/*if(arc != null) {
				vent.setTienearchivo(1);
				idIm = archiver.uploadFile(arc);
				if(idIm != -1) {archivoGuardado = true;}
				repository. archivoApareceEnVenta(idIm, un, vent.getFechainicio());
				repository.save(vent);
				return "{O:Ok}";
			}
			else {*/
				repository.save(sub);
				return "{O:Ok}";
				//return "{E:Es obligatorio insertar al menos una imagen del producto.}";
			//}
		}
		catch(Exception e) {
			if(archivoGuardado) {archiver.deleteFile(idIm);}
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
	@RequestMapping("/listarSubastasCiudad")
	@Produces("application/json")
	List<subasta> listarSubastasCiudad(@RequestParam("ci") String ci){
		return repository.findByciudadAndActivaOrderByFechainicioDesc(ci,1);
	}
	
	@CrossOrigin
	@RequestMapping("/listarSubastasUsuario")
	@Produces("application/json")
	List<subasta> listarSubastasUsuario(@RequestParam("un") String un){
		return repository.findByusuarioOrderByFechainicioDesc(un);
	}
	
	@CrossOrigin
	@RequestMapping("/recuperarSubasta")
	@Produces("application/json")
	Optional<subasta> recuperarSubasta(@RequestParam("id") int id){
		return repository.findByidentificador(id);
	}
	
	@CrossOrigin
	@RequestMapping("/realizarPuja")
	String realizarPuja(@RequestParam("un") String un, @RequestParam("id") int id, @RequestParam("ct") Double ct) {
		Optional<usuario> usaux = userer.recuperarUsuario(un);
		if(!usaux.isPresent()) {
			return "{E:No existe el usuario.}";
		}
		Optional<subasta> aux = repository.findByidentificador(id);
		if(!aux.isPresent()) {
			return "{E:La subasta no existe.}";
		}
		subasta sub = aux.get();
		if(sub.getActiva() == 0) {
			return "{E:La subasta no está activa.}";
		}
		if(sub.getPujaactual() >= ct) {
			return "{E:La puja tiene que ser superior a la puja actual.}";
		}
		try {
			sub.setPujaactual(ct);
			repository.save(sub);
			repository.pujarSubasta(un, id, ct);
		}catch(Exception e) {
			return "{E:Ha habido un problema al realizar la puja.}";
		}
		return "{O:Ok}";
	}
}
