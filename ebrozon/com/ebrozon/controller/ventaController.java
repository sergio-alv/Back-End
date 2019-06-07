 package com.ebrozon.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebrozon.model.venta;
import com.ebrozon.model.usuario;
import com.ebrozon.repository.ofertaRepository;
import com.ebrozon.repository.opinionRepository;
import com.ebrozon.repository.seguimientoRepository;
import com.ebrozon.repository.usuarioRepository;
import com.ebrozon.repository.ventaRepository;
import com.ebrozon.repository.subastaRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import parser.Parseador;
import parser.Stopwords;

@RestController
@Api(value="Sale Management System", description="Operations pertaining to sale in Sale Managament System ")
public class ventaController {
	
	@Autowired
    ventaRepository repository;
	
	@Autowired
   	usuarioRepository repository_u;
	
	@Autowired
    archivoController archiver;
	
	@Autowired
    usuarioController userer;
	
	@Autowired
    subastaController subaster;
	
	@Autowired
    subastaRepository repository_sub;
	
	@Autowired
	ofertaRepository repository_o;
	
	@Autowired
	opinionRepository repository_op;
	
	@Autowired
	seguimientoRepository repository_s;
	
	@Autowired
    etiquetaController etiqueter;
	
	@Autowired
	ventaverantController ventaveranter;
	
	//Publica una venta recibiendo como parámetros nombre de usuario, título del producto, descripción
	//y precio, siendo opcionales los archivos
	@ApiOperation(value = "Publish a sale, returns {O:Ok} if ok or error message if not ok", response = String.class)
	@CrossOrigin
	@RequestMapping("/publicarVenta")
	public String publicarVenta(@ApiParam(value = "username", required = false) @RequestParam("un") String un, 
			@ApiParam(value = "product", required = false) @RequestParam("prod") String prod, 
			@ApiParam(value = "description", required = false) @RequestParam("desc") String desc,
			@ApiParam(value = "price", required = false) @RequestParam("pre") double pre, 
			@ApiParam(value = "image", required = false) @RequestParam(value = "arc1") String arc1, 
			@ApiParam(value = "image", required = false) @RequestParam(value = "arc2", required=false) String arc2, 
			@ApiParam(value = "image", required = false) @RequestParam(value = "arc3", required=false) String arc3,
			@ApiParam(value = "image", required = false) @RequestParam(value = "arc4", required=false) String arc4,
			@ApiParam(value = "category", required = false) @RequestParam("cat") String cat) {
		Optional<usuario> usaux = repository_u.findBynombreusuario(un);
		if(!usaux.isPresent()) {
			return "{E:No existe el usuario.}";
		}
		int idIm = 1;
		int id = 1;
		boolean archivoGuardado = false;
		boolean ventaGuardada = false;
		venta vent;
		try {
			if(usaux.get().getCiudad() != null && !usaux.get().getCiudad().equals("")) {
				vent = new venta(un,prod, desc, pre, 1, 1, new String(usaux.get().getCiudad()));
				vent.setCategoria(cat);
			}
			else {
				vent = new venta(un,prod, desc, pre, 1, 1, new String(usaux.get().getProvincia()));
				vent.setCategoria(cat);
			}
			vent.setProvincia(new String(usaux.get().getProvincia()));
			Optional<Integer> idAux = repository.lastId();
			if(idAux.isPresent()) {
				id = idAux.get()+1;
			}
			vent.setIdentificador(id);
			if(arc1 != null && !arc1.equals("")) {
				vent.setTienearchivo(1);
				usaux = null;
				repository.save(vent);
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
	
	@ApiOperation(value = "Update a sale, returns {O:Ok} if ok or error message if not ok", response = String.class)
	@CrossOrigin
	@RequestMapping("/actualizarVenta")
	public String actualizarVenta(@ApiParam(value = "sale's id", required = false) @RequestParam("id") int id,
			@ApiParam(value = "product", required = false) @RequestParam("prod") String prod, 
			@ApiParam(value = "description", required = false) @RequestParam("desc") String desc,
			@ApiParam(value = "price", required = false) @RequestParam("pre") double pre, 
			@ApiParam(value = "image", required = false) @RequestParam(value = "arc1") String arc1, 
			@ApiParam(value = "image", required = false) @RequestParam(value = "arc2", required=false) String arc2, 
			@ApiParam(value = "image", required = false) @RequestParam(value = "arc3", required=false) String arc3, 
			@ApiParam(value = "image", required = false) @RequestParam(value = "arc4", required=false) String arc4,
			@ApiParam(value = "category", required = false) @RequestParam("cat") String cat) {
		Optional<venta> ventaux = repository.findByidentificador(id);
		if(!ventaux.isPresent()) {
			return "{E:Error inesperado.}";
		}
		int idIm = 1;
		boolean archivoGuardado = false;
		venta vent = ventaux.get();
		ventaveranter.guardar(vent);
		try {
			vent.setProducto(prod);
			repository_o.actualizarProductoOfertas(id,prod);
			vent.setDescripcion(desc);
			vent.setPrecio(pre);
			vent.setCategoria(cat);
			if(arc1 != null && !arc1.equals("")) {
				repository.borrarArchivosVenta(vent.getIdentificador());
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
				
				etiqueter.borrarEtiquetasVenta(id);
				
				Vector etiquetas = new Parseador(prod,new Stopwords()).parsear();
				for(int i = 0; i < etiquetas.size();i++) {
					etiqueter.guardarEtiqueta((String) etiquetas.get(i), vent.getUsuario());
					etiqueter.asignarEtiqueta(id, (String) etiquetas.get(i));
				}
				etiquetas = new Parseador(desc,new Stopwords()).parsear();
				for(int i = 0; i < etiquetas.size();i++) {
					etiqueter.guardarEtiqueta((String) etiquetas.get(i), vent.getUsuario());
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
	
	@ApiOperation(value = "List main page sales, returns list of sales", response = List.class)
	@CrossOrigin
	@RequestMapping("/listarPaginaPrincipal")
	@Produces("application/json")
	List<venta> listarPaginaPrincipal(@ApiParam(value = "sale's id", required = false) @RequestParam("id") int id){
		List<venta> lista = repository.findFirst25ByactivaAndIdentificadorGreaterThanOrderByFechainicioDesc(1,id);
		for(int i = 0; i < lista.size();++i) {
			lista.get(i).setArchivos(repository.listaArchivos(lista.get(i).getIdentificador()));
		}
		return lista;
	}
	
	@ApiOperation(value = "List all city sales, returns list of sales", response = List.class)
	@CrossOrigin
	@RequestMapping("/listarProductosCiudad")
	@Produces("application/json")
	List<venta> listarProductosCiudad(@ApiParam(value = "city", required = false) @RequestParam("ci") String ci,
			@ApiParam(value = "sale's id", required = false) @RequestParam("id") int id){
		List<venta> lista = repository.findFirst25ByciudadAndActivaAndIdentificadorGreaterThanOrderByFechainicioDesc(ci,1,id);
		for(int i = 0; i < lista.size();++i) {
			lista.get(i).setArchivos(repository.listaArchivos(lista.get(i).getIdentificador()));
		}
		return lista;
	}
	
	@ApiOperation(value = "List all province sales, returns list of sales", response = List.class)
	@CrossOrigin
	@RequestMapping("/listarProductosProvincia")
	@Produces("application/json")
	List<venta> listarProductosProvincia(@ApiParam(value = "province", required = false) @RequestParam("pr") String pr, 
			@ApiParam(value = "sale's id", required = false) @RequestParam("id") int id){
		List<venta> lista = repository.findFirst25ByprovinciaAndActivaAndIdentificadorGreaterThanOrderByFechainicioDesc(pr,1,id);
		for(int i = 0; i < lista.size();++i) {
			lista.get(i).setArchivos(repository.listaArchivos(lista.get(i).getIdentificador()));
		}
		return lista;
	}
	
	@ApiOperation(value = "List all user sales, returns list of sales", response = List.class)
	@CrossOrigin
	@RequestMapping("/listarProductosUsuario")
	@Produces("application/json")
	List<venta> listarProductosUsuario(@ApiParam(value = "username", required = false) @RequestParam("un") String un, 
			@ApiParam(value = "sale's id", required = false) @RequestParam("id") int id){
		List<venta> lista = repository.findFirst25ByusuarioAndIdentificadorLessThanAndActivaOrderByFechainicioDesc(un,id,1);
		for(int i = 0; i < lista.size();++i) {
			lista.get(i).setArchivos(repository.listaArchivos(lista.get(i).getIdentificador()));
		}
		return lista;
	}
	
	@ApiOperation(value = "List all purchases of a user, returns list of sales", response = List.class)
	@CrossOrigin
	@RequestMapping("/listarComprasUsuario")
	@Produces("application/json")
	List<venta> listarComprasUsuario(@ApiParam(value = "username", required = false) @RequestParam("un") String un,
			@ApiParam(value = "sale's id", required = false) @RequestParam("id") int id){
		List<venta> lista = repository.findFirst25BycompradorAndIdentificadorLessThanOrderByFechainicioDesc(un,id);
		for(int i = 0; i < lista.size();++i) {
			lista.get(i).setArchivos(repository.listaArchivos(lista.get(i).getIdentificador()));
		}
		return lista;
	}
	
	@ApiOperation(value = "Recover a sale, returns sale", response = venta.class)
	@CrossOrigin
	@RequestMapping("/recuperarProducto")
	@Produces("application/json")
	Optional<venta> recuperarProducto(@ApiParam(value = "sale's id", required = false) @RequestParam("id") int id){
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
	
	String actualizarLatLonVentasUsuario(String un,float lat, float lon) {
		try {
			repository.updateLatLonVentasUsuario(un, lat,lon);
		}
		catch(Exception e) {
			return "{E:Error inesperado.}";
		}
		return "{O:Ok}";
	}
	
	@ApiOperation(value = "Deactivate a sale, returns {O:Ok} if ok or error message if not ok", response = String.class)
	@CrossOrigin
	@RequestMapping("/desactivarVenta")
	String desactivarVenta(@ApiParam(value = "sale's id", required = false) @RequestParam("id") int id) {
		Optional<venta> aux = repository.findByidentificador(id);
		if(aux.isPresent()) {
			if(aux.get().getes_subasta() == 1 && repository_sub.numeroPujasRecibidas(id) >0) {
				return "{E:No se puede borrar una subasta cuando esta ha recibido ya una puja.}";
			}
			venta vent = aux.get();
			vent.setActiva(0);
			repository.save(vent);
			return "{O:Ok}";
		}
		else {
			return "{E:No se ha encontrado la venta.}";
		}
	}
	
	@ApiOperation(value = "Activate a sale, returns {O:Ok} if ok or error message if not ok", response = String.class)
	@CrossOrigin
	@RequestMapping("/activarVenta")
	String activarVenta(@ApiParam(value = "sale's id", required = false) @RequestParam("id") int id) {
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
	
	@ApiOperation(value = "Number of sales of a user, returns number of sales", response = int.class)
	@CrossOrigin
	@RequestMapping("/numeroVentasUsuario")
	int numeroVentasUsuario(@ApiParam(value = "username", required = false) @RequestParam("un") String un) {
		return repository.numeroVentasRealizadas(un);
	}
	
	@ApiOperation(value = "Number of purchases of a user, returns number of purchases", response = int.class)
	@CrossOrigin
	@RequestMapping("/numeroComprasUsuario")
	int numeroComprarUsuario(@ApiParam(value = "username", required = false) @RequestParam("un") String un) {
		return repository.numeroComprasRealizadas(un);
	}
	
	@ApiOperation(value = "Confirm the payment of a sale, returns {O:Ok} if ok or error message if not ok", response = String.class)
	@CrossOrigin
	@RequestMapping("/confirmarPagoVenta")
	String confirmarPagoVenta(@ApiParam(value = "sale's id", required = false) @RequestParam("id") int id) {
		Optional<venta> aux = repository.findByidentificador(id);
		if(!aux.isPresent()) {
			return "{E:La venta no existe}";
		}
		aux.get().setFechapago(new Date());
		repository.save(aux.get());
		repository_o.confirmarPagoVenta(id);
		return "{O:Ok}";
	}
	
	@ApiOperation(value = "Cancel the payment of a sale, returns {O:Ok} if ok or error message if not ok", response = String.class)
	@CrossOrigin
	@RequestMapping("/cancelarPagoVenta")
	String cancelarPagoVenta(@ApiParam(value = "sale's id", required = false) @RequestParam("id") int id) {
		Optional<venta> aux = repository.findByidentificador(id);
		if(!aux.isPresent()) {
			return "{E:La venta no existe}";
		}
		aux.get().setFechaventa(null);
		aux.get().setActiva(1);
		aux.get().setComprador(null);
		aux.get().setFechapago(null);		
		aux.get().setPreciofinal(0);
		repository.save(aux.get());
		repository_o.cancelarPagoVenta(id);
		return "{O:Ok}";
	}
	
	public static double distanciaCoord(double lat1, double lng1, double lat2, double lng2) {  
        //double radioTierra = 3958.75;//en millas  
        double radioTierra = 6371;//en kilómetros  
        double dLat = Math.toRadians(lat2 - lat1);  
        double dLng = Math.toRadians(lng2 - lng1);  
        double sindLat = Math.sin(dLat / 2);  
        double sindLng = Math.sin(dLng / 2);  
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)  
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));  
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));  
        double distancia = radioTierra * va2;  
   
        return distancia;  
    } 
	
	@ApiOperation(value = "List sales, returns list of sales",notes="met = Fecha des, Fecha asc, Precio des, Precio asc, Coincidencias, Valoraciones, Popularidad, Distancia", response = List.class)
	@CrossOrigin
	@RequestMapping("/listarProductos")
	@Produces("application/json")
	List<venta> listarProductosEtiquetas(@ApiParam(value = "", required = false) @RequestParam(value = "ets", required=false) String ets,
			@ApiParam(value = "search type", required = false) @RequestParam("met") String met,
			@ApiParam(value = "min price", required = false) @RequestParam(value = "min", required=false) Double min,
			@ApiParam(value = "max price", required = false) @RequestParam(value = "max", required=false) Double max,
			@ApiParam(value = "province", required = false)@RequestParam(value = "pr", required=false) String pr, 
			@ApiParam(value = "city", required = false) @RequestParam(value = "ci", required=false) String ci,
			@ApiParam(value = "sale's id", required = false) @RequestParam(value = "id", required=false) Integer id, 
			@ApiParam(value = "sale's type", required = false) @RequestParam(value = "tp", required=false) Integer tp,
			@ApiParam(value = "category", required = false) @RequestParam(value = "cat", required=false) String cat,
			@ApiParam(value = "latitude", required = false) @RequestParam(value = "lat", required=false) Float lat,
			@ApiParam(value = "longitude", required = false) @RequestParam(value = "lon", required=false) Float lon,
			@ApiParam(value = "minimal distance", required = false) @RequestParam(value = "mind", required=false) Float mind,
			@ApiParam(value = "maximum distance", required = false) @RequestParam(value = "maxd", required=false) Float maxd){
		List<venta> lista = new ArrayList<venta>();
		List<venta> listaBuena = new ArrayList<venta>();
		Vector etiquetas = null;
		if(ets!= null) {
			etiquetas = new Parseador(ets,new Stopwords()).parsear();
		}
		if(met.equals("Coincidencias") && (etiquetas == null || etiquetas.isEmpty())) {
			return null;
		}
		else if(met.equals("Coincidencias")){
			int idm = 99999999;
			if(id != null) {idm = id;}
			List<Integer> nvs = etiqueter.ventasConEtiquetasOrdenadasPorCoincidencias(etiquetas);
			boolean ok = (id == null) || !nvs.contains(idm);
			for(int i = 0; i < nvs.size(); ++i) {
				if(ok) {
					lista.add(repository.findByidentificador(nvs.get(i)).get());
				}
				if(nvs.get(i) == idm) {ok = true;}
			}
		}
		else if(met.equals("Popularidad") || met.equals("Valoraciones")){
			int idm = 99999999;
			if(id != null) {idm = id;}
			List<Integer> nvs = null;
			if(met.equals("Popularidad")) {
				nvs= repository_s.ventasPorSeguimientos();
			}
			else {
				nvs = repository_op.ventasPorValoraciones();
			}
			boolean ok = (id == null) || !nvs.contains(idm);
			if(ets == null) {
				for(int i = 0; i < nvs.size(); ++i) {
					if(ok) {
						lista.add(repository.findByidentificador(nvs.get(i)).get());
					}
					if(nvs.get(i) == idm) {ok = true;}
				}
			}
			else {
				List<Integer> nve  = etiqueter.ventasConEtiquetas(etiquetas);
				for(int i = 0; i < nvs.size(); ++i) {
					if(ok && nve.contains(nvs.get(i))) {
						lista.add(repository.findByidentificador(nvs.get(i)).get());
					}
					if(nvs.get(i) == idm) {ok = true;}
				}
			}
		}
		else if(met.equals("Precio asc")){
			int idm = 0;
			if(id != null) {idm = id;}
			Optional<venta> vaux = repository.findByidentificador(idm);
			double p = 0;
			if(vaux.isPresent()) {
				p = vaux.get().getPrecio();
			}
			if(ets != null) {
				List<Integer> nvs = etiqueter.ventasConEtiquetas(etiquetas);
				lista = repository.findByidentificadorInAndPrecioGreaterThanOrPrecioEqualsAndIdentificadorGreaterThanOrderByPrecioAsc(nvs,p,p,idm);
			}
			else {
				lista = repository.findByprecioGreaterThanOrPrecioEqualsAndIdentificadorGreaterThanOrderByPrecioAsc(p,p,idm);
			}
		}
		else if(met.equals("Precio des")){
			int idm = 99999999;
			if(id != null) {idm = id;}
			Optional<venta> vaux = repository.findByidentificador(idm);
			double p = 99999999;
			if(vaux.isPresent()) {
				p = vaux.get().getPrecio();
			}
			if(ets != null) {
				List<Integer> nvs = etiqueter.ventasConEtiquetas(etiquetas);
				lista = repository.findByidentificadorInAndPrecioLessThanOrPrecioEqualsAndIdentificadorGreaterThanOrderByPrecioDesc(nvs,p,p,idm);
			}
			else {
				lista = repository.findByprecioLessThanOrPrecioEqualsAndIdentificadorGreaterThanOrderByPrecioDesc(p,p,idm);
			}
		}
		else if(met.equals("Fecha asc")){
			int idm = 0;
			if(id != null) {idm = id;}
			if(ets != null) {
				List<Integer> nvs = etiqueter.ventasConEtiquetas(etiquetas);
				lista = repository.findByidentificadorInAndIdentificadorGreaterThanOrderByFechainicioAsc(nvs,idm);
			}
			else {
				lista = repository.findByidentificadorGreaterThanOrderByFechainicioAsc(idm);
			}
		}
		else if(met.equals("Fecha des") || met.equals("Distancia")){
			int idm = 99999999;
			if(id != null) {idm = id;}
			if(ets != null) {
				List<Integer> nvs = etiqueter.ventasConEtiquetas(etiquetas);
				lista = repository.findByidentificadorInAndIdentificadorLessThanOrderByFechainicioDesc(nvs,idm);
			}
			else {
				lista = repository.findByidentificadorLessThanOrderByFechainicioDesc(idm);
			}
		}
		else {
			return lista;
		}
		
		for(int i = 0; i < lista.size();++i) {
			if(lista.get(i).getLatitud() != null && lista.get(i).getLongitud() != null &&
				lista.get(i).getLatitud() != -9999 && lista.get(i).getLongitud() != -9999
				&& lat != null && lon != null) {
				lista.get(i).setDistancia((float) distanciaCoord(lista.get(i).getLatitud(), lista.get(i).getLongitud(),lat,lon));
			}
			else {
				lista.get(i).setDistancia(999999);
			}
		}
		
		if(met.equals("Distancia")) {
			Collections.sort(lista);
		}
		
		double pmin = 0;
		double pmax = 99999999;
		if(min != null) {
			pmin = min;
		}
		if(max != null) {
			pmax = max;
		}
		int count = 0;
		for(int i = 0; count < 25 && i < lista.size();++i) {
			venta aux = lista.get(i);
			if(aux.getActiva() == 1 && aux.getPrecio() >= pmin && aux.getPrecio() <= pmax &&
					(pr == null || (pr != null && aux.getProvincia().equals(pr))) &&
					(ci == null || (ci != null && aux.getCiudad().equals(ci))) &&
					(tp == null || (tp != null && aux.getes_subasta() == tp)) &&
					(cat == null || (cat != null && aux.getCategoria().equals(cat))) &&
					(maxd == null || (maxd != null && aux.getDistancia() <= maxd)) &&
					(mind == null || (mind != null && aux.getDistancia() >= mind))
					) {
				listaBuena.add(lista.get(i));
				++count;
			}
		}
		
		for(int i = 0; i < listaBuena.size();++i) {
			listaBuena.get(i).setArchivos(repository.listaArchivos(listaBuena.get(i).getIdentificador()));
		}
		
		return listaBuena;
	}
}