package com.ebrozon.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.MessageDigest;

import com.ebrozon.model.usuario;
import com.ebrozon.repository.usuarioRepository;

@RestController
public class usuarioController {
	@Autowired
    usuarioRepository repository;
	
	@Autowired
    archivoController archiver;
	
	/**
	*Registra a un usuario recibiendo como parámetros obligatorios el nombre de usuario, el correo
	*la contraseña, el nombre y los apellidos, y siendo opcionales el teléfono, el código postal
	*la ciudad, la provincia, latitud y longitud, y la imagen de perfil.
	*http://localhost:8080/registrar?un=karny2&pass=caca&cor=cececw@gmail.com&na=saul&lna=alarcon
	*/
	@RequestMapping("/registrar")
    public String registrar(@RequestParam("un") String un, @RequestParam("cor") String cor, @RequestParam("pass") String pass,
    						@RequestParam(value = "tel", required=false) Integer tel, @RequestParam("na") String na, @RequestParam("lna") String lna,
    						@RequestParam(value = "cp", required=false) Integer cp, @RequestParam(value = "ci", required=false) String ci, @RequestParam(value = "pr", required=false) String pr,
    						@RequestParam(value = "lat", required=false) String lat, @RequestParam(value = "lon", required=false) String lon, @RequestParam(value = "im", required=false) MultipartFile im
    						){
		
		if(repository.existsBynombreusuario(un)) {
			return "El nombre de usuario no está disponible.";
		}
		if(repository.existsBycorreo(cor)) {
			return "El correo electrónico no está disponible.";
		}
		
		usuario user;
		
		MessageDigest md;
    	StringBuffer sb = new StringBuffer();
	    try {
	    	md = MessageDigest.getInstance("MD5");
	    	md.update(pass.trim().getBytes());
			byte[] digest = md.digest();
			sb = new StringBuffer();
			for (byte b : digest) {
				sb.append(String.format("%02x", b & 0xff));
			}
    	}
    	catch(Exception e) {
    		return "Ha habido un problema durante el registro.";
    	}
		
		try {
	        user = new usuario(un, cor, sb.toString(), na, lna);
	        if (tel != null){ 
	        	user.setTelefono(tel);
	        }
	        
	        if (cp != null){ 
	        	user.setCodigopostal(cp);
	        }
	        
	        if ((ci != null) && (!ci.trim().equals(""))){ 
	        	user.setCiudad(ci);
	        }
	        
	        if ((pr != null) && (!pr.trim().equals(""))){ 
	        	user.setProvincia(pr);
	        }
	        
	        if ((lat != null) && (!lat.trim().equals("")) && (lon != null) && (!lon.trim().equals(""))){ 
	        	user.setLatitud(Float.parseFloat(lat));
	        	user.setLongitud(Float.parseFloat(lon));
	        }
	        if (im != null && !im.isEmpty()){ 
	        	int idIm = archiver.uploadFile(im);
	        	user.setArchivo(idIm);
	        }
	        else {
	        	user.setArchivo(0);
	        }
	        
	        user.setActivo(1);
		}
		catch(Exception e){
			return e.getMessage();
		}
        
        repository.save(user);
        
        return "Ok";
    }
	
	/**
	*Actualiza la información de un usuario recibiendo como parámetros obligatorios el nombre de usuario, el correo
	*la contraseña, el nombre y los apellidos, y siendo opcionales el teléfono, el código postal
	*la ciudad, la provincia, latitud y longitud, y la imagen de perfil.
	*/
	@RequestMapping("/actualizarUsuario")
    public String actualizarUsuario(@RequestParam("un") String un,
    						@RequestParam("tel") Integer tel, @RequestParam("name") String na, @RequestParam("lna") String lna,
    						@RequestParam("cp") Integer cp, @RequestParam("ci") String ci, @RequestParam("pr") String pr,
    						@RequestParam("lat") String lat, @RequestParam("lon") String lon, @RequestParam("im") MultipartFile im
    						){
		
		if(!repository.existsBynombreusuario(un)) {
			return "El nombre de usuario no exite.";
		}
		
		usuario user = repository.findBynombreusuario(un).get();
		
		try {
			user.setNombre(na);
	        user.setApellidos(lna);
	        
	        if (tel != null){ 
	        	user.setTelefono(tel);
	        }
	        
	        if (cp != null){ 
	        	user.setCodigopostal(cp);
	        }
	        
	        if ((ci != null) && (!ci.trim().equals(""))){ 
	        	user.setCiudad(ci);
	        }
	        
	        if ((pr != null) && (!pr.trim().equals(""))){ 
	        	user.setProvincia(pr);
	        }
	        
	        if ((lat != null) && (!lat.trim().equals("")) && (lon != null) && (!lon.trim().equals(""))){ 
	        	user.setLatitud(Float.parseFloat(lat));
	        	user.setLongitud(Float.parseFloat(lon));
	        }
	        if (!im.isEmpty()){ 
	        	int idIm = archiver.uploadFile(im);
	        	user.setArchivo(idIm);
	        }
		}
		catch(Exception e){
			return e.getMessage();
		}
        
        repository.save(user);
        
        return "Ok";
    }
	
	/**
	*Comprueba la información del usuario para logearse, recibiendo como parámetros su
	*nombre de usuario y su contraseña
	*http://localhost:8080/logear?un=karny1&pass=caca
	*/
	@RequestMapping("/logear")
	public String logear(@RequestParam("un") String un, @RequestParam("pass") String pass) {
		Optional<usuario> aux = repository.findBynombreusuario(un);
		if(aux.isPresent()) {
			if(aux.get().getActivo() == 0) {
				return "La cuenta está deshabilitada.";
			}
			MessageDigest md;
	    	StringBuffer sb = new StringBuffer();
		    try {
		    	md = MessageDigest.getInstance("MD5");
		    	md.update(pass.trim().getBytes());
				byte[] digest = md.digest();
				sb = new StringBuffer();
				for (byte b : digest) {
					sb.append(String.format("%02x", b & 0xff));
				}
	    	}
	    	catch(Exception e) {
	    		return "Ha habido un problema durante el login.";
	    	}
		    
		    if(sb.toString().equals(aux.get().getContrasena())) {
		    	repository.registrarLogin(un);
		    	return "Ok";
		    }
		    else {
		    	return "La contraseña es incorrecta.";
		    }
		}
		else {
			return "El nombre usuario no existe.";
		}
	}
	
	/**
	*Recupera la información de un usuario dado su nombre de usuario
	*/
	@RequestMapping("/recuperarUsuario")
	public usuario recuperarUsuario(@RequestParam("un") String un) {
		Optional<usuario> aux = repository.findBynombreusuario(un);
		if(aux.isPresent()) {
			usuario user = aux.get();
			user.setUrlArchivo(archiver.loadFile(user.getArchivo()));
			return user;
		}
		else {
			return null;
		}
	}
	
	/**
	*Desactiva la cuenta del usuario identificado por el nombre de usuario dado
	*/
	@RequestMapping("/banearUsuario")
	public String banearUsuario(@RequestParam("un") String un) {
		Optional<usuario> aux = repository.findBynombreusuario(un);
		if(aux.isPresent()) {
			usuario user = aux.get();
			user.setActivo(0);
			repository.save(user);
			return "Ok";
		}
		else {
			return "No se ha encontrado el usuario";
		}
	}
	
	/**
	*Activa la cuenta del usuario identificado por el nombre de usuario dado
	*/
	@RequestMapping("/desbanearUsuario")
	public String desbanearUsuario(@RequestParam("un") String un) {
		Optional<usuario> aux = repository.findBynombreusuario(un);
		if(aux.isPresent()) {
			usuario user = aux.get();
			user.setActivo(1);
			repository.save(user);
			return "Ok";
		}
		else {
			return "No se ha encontrado el usuario";
		}
	}
}
