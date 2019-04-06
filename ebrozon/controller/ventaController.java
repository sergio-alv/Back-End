package com.ebrozon.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

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
	
	//Publica una venta recibiendo como parámetros nombre de usuario, título del producto, descripción
	//y precio, siendo opcionales los archivos
	@RequestMapping("/publicarVenta")
	public String publicarVenta(@RequestParam("un") String un, @RequestParam("prod") String prod, @RequestParam("desc") String desc,
			@RequestParam("pre") double pre, @RequestParam(value = "arc", required=false) MultipartFile arc) {
		Optional<usuario> usaux = userer.recuperarUsuario(un);
		if(!usaux.isPresent()) {
			return "{E:No existe el usuario.}";
		}
		int idIm = -1;
		boolean archivoGuardado = false;
		venta vent;
		try {
			vent = new venta(un,prod, desc, pre, 0, 1, usaux.get().getCiudad());
			if(arc != null) {
				vent.setTienearchivo(1);
				idIm = archiver.uploadFile(arc);
				if(idIm != -1) {archivoGuardado = true;}
				repository. archivoApareceEnVenta(idIm, un, vent.getFechainicio());
				repository.save(vent);
				return "{O:Ok}";
			}
			else {
				repository.save(vent);
				return "{O:Ok}";
				//return "{E:Es obligatorio insertar al menos una imagen del producto.}";
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
	@RequestMapping("/listarProductosCiudad")
	List<venta> listaProductosCiudad(@RequestParam("ci") String ci){
		return repository.findByciudad(ci);
	}
	
	Optional<venta> recuperarProducto(@RequestParam("id") int id){
		return repository.findByidentificador(id);
	}
}
