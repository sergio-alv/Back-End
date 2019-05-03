package com.ebrozon.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ebrozon.model.subasta;
import com.ebrozon.model.usuario;
import com.ebrozon.repository.subastaRepository;

@RestController
@Configuration
@EnableScheduling
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
			@RequestParam(value = "arc1") String arc1, @RequestParam(value = "arc2", required=false) String arc2
			, @RequestParam(value = "arc3", required=false) String arc3, @RequestParam(value = "arc4", required=false) String arc4) {
		Optional<usuario> usaux = userer.recuperarUsuario(un);
		if(!usaux.isPresent()) {
			return "{E:No existe el usuario.}";
		}
		int idIm = 1;
		int id = 1;
		boolean archivoGuardado = false;
		boolean ventaGuardada = false;
		subasta sub;
		try {
			sub = new subasta(un,prod, desc, pre, 1, 1, usaux.get().getCiudad(),new Date(),pin,pin);
			Optional<Integer> idAux = repository.lastId();
			if(idAux.isPresent()) {
				id = idAux.get()+1;
			}
			sub.setIdentificador(id);
			if(arc1 != null) {
				sub.setTienearchivo(1);
				repository.save(sub);
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
	@RequestMapping("/actualizarSubasta")
	public String actualizarSubasta(@RequestParam("id") int id, @RequestParam("prod") String prod, @RequestParam("desc") String desc,
			@RequestParam("pre") double pre, @RequestParam("end") /*Date*/String end,  @RequestParam("pin") double pin, 
			@RequestParam(value = "arc1") String arc1, @RequestParam(value = "arc2", required=false) String arc2
			, @RequestParam(value = "arc3", required=false) String arc3, @RequestParam(value = "arc4", required=false) String arc4) {
		
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
			if(arc1 != null) {
				sub.setTienearchivo(1);
				repository.save(sub);
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
	List<subasta> listarSubastasCiudad(@RequestParam("ci") String ci, @RequestParam("id") int id){
		return repository.findFirst25ByciudadAndActivaAndIdentificadorGreaterThanOrderByFechainicioDesc(ci,1,id);
	}
	
	@CrossOrigin
	@RequestMapping("/listarSubastasProvincia")
	@Produces("application/json")
	List<subasta> listarSubastasProvincia(@RequestParam("pr") String pr, @RequestParam("id") int id){
		return repository.findFirst25ByprovinciaAndActivaAndIdentificadorGreaterThanOrderByFechainicioDesc(pr,1,id);
	}
	
	@CrossOrigin
	@RequestMapping("/listarSubastasUsuario")
	@Produces("application/json")
	List<subasta> listarSubastasUsuario(@RequestParam("un") String un, @RequestParam("id") int id){
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
	
	@Scheduled(cron = "* * * * * *", zone = "CET")
	void cerrarSubastas() {
		List<subasta> list = repository.findByactiva(1);
		for(int i = 0; i < list.size();++i) {
			if(new Date().after(list.get(i).getFechafin())) {
				list.get(i).setActiva(0);
				repository.save(list.get(i));
			}
		}
	}
}
