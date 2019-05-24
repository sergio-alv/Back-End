 package com.ebrozon.controller;

import java.util.ArrayList;
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

import parser.Parseador;
import parser.Stopwords;

@RestController
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
	@CrossOrigin
	@RequestMapping("/publicarVenta")
	public String publicarVenta(@RequestParam("un") String un, @RequestParam("prod") String prod, @RequestParam("desc") String desc,
			@RequestParam("pre") double pre, @RequestParam(value = "arc1") String arc1, @RequestParam(value = "arc2", required=false) String arc2
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
	
	@CrossOrigin
	@RequestMapping("/actualizarVenta")
	public String actualizarVenta(@RequestParam("id") int id, @RequestParam("prod") String prod, @RequestParam("desc") String desc,
			@RequestParam("pre") double pre, @RequestParam(value = "arc1") String arc1, @RequestParam(value = "arc2", required=false) String arc2
			, @RequestParam(value = "arc3", required=false) String arc3, @RequestParam(value = "arc4", required=false) String arc4,
			@RequestParam("cat") String cat) {
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
	
	@CrossOrigin
	@RequestMapping("/listarPaginaPrincipal")
	@Produces("application/json")
	List<venta> listarPaginaPrincipal(@RequestParam("id") int id){
		List<venta> lista = repository.findFirst25ByactivaAndIdentificadorGreaterThanOrderByFechainicioDesc(1,id);
		for(int i = 0; i < lista.size();++i) {
			lista.get(i).setArchivos(repository.listaArchivos(lista.get(i).getIdentificador()));
		}
		return lista;
	}
	
	@CrossOrigin
	@RequestMapping("/listarProductosCiudad")
	@Produces("application/json")
	List<venta> listarProductosCiudad(@RequestParam("ci") String ci, @RequestParam("id") int id){
		List<venta> lista = repository.findFirst25ByciudadAndActivaAndIdentificadorGreaterThanOrderByFechainicioDesc(ci,1,id);
		for(int i = 0; i < lista.size();++i) {
			lista.get(i).setArchivos(repository.listaArchivos(lista.get(i).getIdentificador()));
		}
		return lista;
	}
	
	@CrossOrigin
	@RequestMapping("/listarProductosProvincia")
	@Produces("application/json")
	List<venta> listarProductosProvincia(@RequestParam("pr") String pr, @RequestParam("id") int id){
		List<venta> lista = repository.findFirst25ByprovinciaAndActivaAndIdentificadorGreaterThanOrderByFechainicioDesc(pr,1,id);
		for(int i = 0; i < lista.size();++i) {
			lista.get(i).setArchivos(repository.listaArchivos(lista.get(i).getIdentificador()));
		}
		return lista;
	}
	
	@CrossOrigin
	@RequestMapping("/listarProductosUsuario")
	@Produces("application/json")
	List<venta> listarProductosUsuario(@RequestParam("un") String un, @RequestParam("id") int id){
		List<venta> lista = repository.findFirst25ByusuarioAndIdentificadorLessThanAndActivaOrderByFechainicioDesc(un,id,1);
		for(int i = 0; i < lista.size();++i) {
			lista.get(i).setArchivos(repository.listaArchivos(lista.get(i).getIdentificador()));
		}
		return lista;
	}
	
	@CrossOrigin
	@RequestMapping("/listarComprasUsuario")
	@Produces("application/json")
	List<venta> listarComprasUsuario(@RequestParam("un") String un, @RequestParam("id") int id){
		List<venta> lista = repository.findFirst25BycompradorAndIdentificadorLessThanOrderByFechainicioDesc(un,id);
		for(int i = 0; i < lista.size();++i) {
			lista.get(i).setArchivos(repository.listaArchivos(lista.get(i).getIdentificador()));
		}
		return lista;
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
	
	@CrossOrigin
	@RequestMapping("/numeroVentasUsuario")
	int numeroVentasUsuario(@RequestParam("un") String un) {
		return repository.numeroVentasRealizadas(un);
	}
	
	@CrossOrigin
	@RequestMapping("/numeroComprasUsuario")
	int numeroComprarUsuario(@RequestParam("un") String un) {
		return repository.numeroComprasRealizadas(un);
	}
	
	@CrossOrigin
	@RequestMapping("/confirmarPagoVenta")
	String confirmarPagoVenta(@RequestParam("id") int id) {
		Optional<venta> aux = repository.findByidentificador(id);
		if(!aux.isPresent()) {
			return "{E:La venta no existe}";
		}
		aux.get().setFechapago(new Date());
		repository.save(aux.get());
		return "{O:Ok}";
	}
	
	@CrossOrigin
	@RequestMapping("/cancelarPagoVenta")
	String cancelarPagoVenta(@RequestParam("id") int id) {
		Optional<venta> aux = repository.findByidentificador(id);
		if(!aux.isPresent()) {
			return "{E:La venta no existe}";
		}
		aux.get().setFechaventa(null);
		aux.get().setActiva(1);
		aux.get().setComprador(null);
		repository.save(aux.get());
		return "{O:Ok}";
	}
	
	@CrossOrigin
	@RequestMapping("/listarProductos")
	@Produces("application/json")
	List<venta> listarProductosEtiquetas(@RequestParam(value = "ets", required=false) String ets, @RequestParam("met") String met,
			@RequestParam(value = "min", required=false) Double min,@RequestParam(value = "max", required=false) Double max,
			@RequestParam(value = "pr", required=false) String pr, @RequestParam(value = "ci", required=false) String ci,
			@RequestParam(value = "id", required=false) Integer id, @RequestParam(value = "tp", required=false) Integer tp,
			@RequestParam(value = "cat", required=false) String cat){
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
		else if(met.equals("Fecha des")){
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
					(cat == null || (cat != null && aux.getCategoria().equals(cat)))) {
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