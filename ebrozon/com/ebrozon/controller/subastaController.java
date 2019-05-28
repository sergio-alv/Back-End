package com.ebrozon.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;

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

import com.ebrozon.model.subasta;
import com.ebrozon.model.usuario;
import com.ebrozon.model.venta;
import com.ebrozon.repository.ofertaRepository;
import com.ebrozon.repository.subastaRepository;
import com.ebrozon.repository.usuarioRepository;
import com.ebrozon.repository.ventaRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import parser.Parseador;
import parser.Stopwords;

@RestController
@Configuration
@EnableScheduling
@Api(value="Auction Management System", description="Operations pertaining to auction in Auction Managament System ")
public class subastaController {
	
	@Autowired
    subastaRepository repository;
	
	@Autowired
    ventaRepository repository_v;
	
	@Autowired
    archivoController archiver;
	
	@Autowired
    etiquetaController etiqueter;
	
	@Autowired
    usuarioController userer;
	
	@Autowired
   	usuarioRepository repository_u;

	@Autowired
	ofertaRepository repository_o;
	
	@Autowired
	ventaverantController ventaveranter;
	
	//Publica una venta recibiendo como parámetros nombre de usuario, título del producto, descripción
	//y precio, siendo opcionales los archivos
	@ApiOperation(value = "Publish an auction", response = String.class)
	@CrossOrigin
	@RequestMapping("/publicarSubasta")
	public String publicarSubasta(@RequestParam("un") String un, @RequestParam("prod") String prod, @RequestParam("desc") String desc,
			@RequestParam("pre") double pre, @RequestParam("end") long end,  @RequestParam("pin") double pin, 
			@RequestParam(value = "arc1") String arc1, @RequestParam(value = "arc2", required=false) String arc2
			, @RequestParam(value = "arc3", required=false) String arc3, @RequestParam(value = "arc4", required=false) String arc4,
			@RequestParam("cat") String cat) {
		Optional<usuario> usaux = repository_u.findBynombreusuario(un);
		if(!usaux.isPresent()) {
			return "{E:No existe el usuario.}";
		}
		int idIm = 1;
		int id = 1;
		boolean archivoGuardado = false;
		boolean ventaGuardada = false;
		subasta sub;
		try {
			if(usaux.get().getCiudad() != null && !usaux.get().getCiudad().contentEquals("")) {
				sub = new subasta(un,prod, desc, pre, 1, 1, new String(usaux.get().getCiudad()),new Date(end),pin,pin);
				sub.setCategoria(cat);
			}
			else {
				sub = new subasta(un,prod, desc, pre, 1, 1, new String(usaux.get().getProvincia()),new Date(end),pin,pin);
				sub.setCategoria(cat);
			}
			sub.setProvincia(new String(usaux.get().getProvincia()));
			
			Optional<Integer> idAux = repository.lastId();
			if(idAux.isPresent()) {
				id = idAux.get()+1;
			}
			sub.setIdentificador(id);
			if(arc1 != null && !arc1.equals("")) {
				sub.setTienearchivo(1);
				usaux = null;
				repository.save(sub);
				ventaGuardada = true;
				idIm = archiver.uploadArchivoTemp(arc1);
				if(idIm != -1) {archivoGuardado = true;}
				repository.archivoApareceEnVenta(idIm, id);
				if(arc2 != null && !arc2.equals("")) {
					idIm = archiver.uploadArchivoTemp(arc2);
					repository.archivoApareceEnVenta(idIm, id);
				}
				if(arc3 != null && !arc3.equals("")) {
					idIm = archiver.uploadArchivoTemp(arc3);
					repository.archivoApareceEnVenta(idIm, id);
				}
				if(arc4 != null && !arc4.equals("")) {
					idIm = archiver.uploadArchivoTemp(arc4);
					repository.archivoApareceEnVenta(idIm, id);
				}
				
				//ETIQUETAS
				Vector etiquetas = new Parseador(prod,new Stopwords()).parsear();
				for(int i = 0; i < etiquetas.size();i++) {
					etiqueter.guardarEtiqueta((String) etiquetas.get(i), un);
					etiqueter.asignarEtiqueta(id, (String) etiquetas.get(i));
				}
				etiquetas = new Parseador(desc,new Stopwords()).parsear();
				for(int i = 0; i < etiquetas.size();i++) {
					etiqueter.guardarEtiqueta((String) etiquetas.get(i), un);
					etiqueter.asignarEtiqueta(id, (String) etiquetas.get(i));
				}
				
				//--------
				
				List<Integer> e = repository.listaArchivos(id);
				if(e.isEmpty()) {
					repository.deleteByidentificador(id);
					return "{E:Problema al subir las imágenes, no se ha subido la venta.}";
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
	
	@ApiOperation(value = "Update an auction", response = String.class)
	@CrossOrigin
	@RequestMapping("/actualizarSubasta")
	public String actualizarSubasta(@RequestParam("id") int id, @RequestParam("prod") String prod, @RequestParam("desc") String desc,
			@RequestParam("pre") double pre, @RequestParam("end") long end,  @RequestParam("pin") double pin, 
			@RequestParam(value = "arc1") String arc1, @RequestParam(value = "arc2", required=false) String arc2
			, @RequestParam(value = "arc3", required=false) String arc3, @RequestParam(value = "arc4", required=false) String arc4,
			@RequestParam("cat") String cat) {
		
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
		ventaveranter.guardar((venta)sub);
		try {
			sub.setProducto(prod);
			repository_o.actualizarProductoOfertas(id,prod);
			sub.setDescripcion(desc);
			sub.setPrecio(pre);
			sub.setFechafin(new Date(end));
			sub.setPrecioinicial(pin);
			sub.setPujaactual(pin);
			sub.setCategoria(cat);
			if(arc1 != null && !arc1.equals("")) {
				repository_v.borrarArchivosVenta(sub.getIdentificador());
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
				
				etiqueter.borrarEtiquetasVenta(id);
				
				Vector etiquetas = new Parseador(prod,new Stopwords()).parsear();
				for(int i = 0; i < etiquetas.size();i++) {
					etiqueter.guardarEtiqueta((String) etiquetas.get(i), sub.getUsuario());
					etiqueter.asignarEtiqueta(id, (String) etiquetas.get(i));
				}
				etiquetas = new Parseador(desc,new Stopwords()).parsear();
				for(int i = 0; i < etiquetas.size();i++) {
					etiqueter.guardarEtiqueta((String) etiquetas.get(i), sub.getUsuario());
					etiqueter.asignarEtiqueta(id, (String) etiquetas.get(i));
				}
				
				List<Integer> e = repository.listaArchivos(id);
				if(e.isEmpty()) {
					repository.deleteByidentificador(id);
					return "{E:Problema al subir las imágenes, no se ha subido la venta.}";
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
	
	@ApiOperation(value = "List all auctions of a city", response = List.class)
	@CrossOrigin
	@RequestMapping("/listarSubastasCiudad")
	@Produces("application/json")
	List<subasta> listarSubastasCiudad(@RequestParam("ci") String ci, @RequestParam("id") int id){
		List<subasta> lista = repository.findFirst25ByciudadAndActivaAndIdentificadorGreaterThanOrderByFechainicioDesc(ci,1,id);
		for(int i = 0; i < lista.size();++i) {
			lista.get(i).setArchivos(repository.listaArchivos(lista.get(i).getIdentificador()));
		}
		return lista;
	}
	
	@ApiOperation(value = "List all auctions of a province", response = List.class)
	@CrossOrigin
	@RequestMapping("/listarSubastasProvincia")
	@Produces("application/json")
	List<subasta> listarSubastasProvincia(@RequestParam("pr") String pr, @RequestParam("id") int id){
		List<subasta> lista = repository.findFirst25ByprovinciaAndActivaAndIdentificadorGreaterThanOrderByFechainicioDesc(pr,1,id);
		for(int i = 0; i < lista.size();++i) {
			lista.get(i).setArchivos(repository.listaArchivos(lista.get(i).getIdentificador()));
		}
		return lista;
	}
	
	@ApiOperation(value = "List all auctions of a user", response = List.class)
	@CrossOrigin
	@RequestMapping("/listarSubastasUsuario")
	@Produces("application/json")
	List<subasta> listarSubastasUsuario(@RequestParam("un") String un, @RequestParam("id") int id){
		List<subasta> lista  = repository.findByusuarioOrderByFechainicioDesc(un);
		for(int i = 0; i < lista.size();++i) {
			lista.get(i).setArchivos(repository.listaArchivos(lista.get(i).getIdentificador()));
		}
		return lista;
	}
	
	@ApiOperation(value = "Recover an auction", response = subasta.class)
	@CrossOrigin
	@RequestMapping("/recuperarSubasta")
	@Produces("application/json")
	Optional<subasta> recuperarSubasta(@RequestParam("id") int id){
		Optional<subasta> aux = repository.findByidentificador(id);
		if(aux.isPresent()) {
			aux.get().setArchivos(repository.listaArchivos(id));
		}
		return aux;
	}
	
	@ApiOperation(value = "List all earned and pending auctions of a user", response = List.class)
	@CrossOrigin
	@RequestMapping("/listarSubastasGanadasPendientes")
	@Produces("application/json")
	List<subasta> listarSubastasGanadasPendientes(@RequestParam("un") String un){
		return repository.findBycompradorAndActivaAndFechapagoIsNull(un, 0);
	}
	
	@ApiOperation(value = "List all finished and pending auctions of a user", response = List.class)
	@CrossOrigin
	@RequestMapping("/listarSubastasTerminadasPendientes")
	@Produces("application/json")
	List<subasta> listarSubastasTerminadasPendientes(@RequestParam("un") String un){
		return repository.findByusuarioAndActivaAndFechapagoIsNull(un, 0);
	}
	
	@ApiOperation(value = "Place a bid", response = String.class)
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
				Optional<String> aux = repository.pujadorGanador(list.get(i).getIdentificador());
				if(aux.isPresent()) {
					list.get(i).setComprador(aux.get());
					list.get(i).setPreciofinal(list.get(i).getPujaactual());
				}
				repository.save(list.get(i));
			}
		}
	}
}
