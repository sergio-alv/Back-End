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
	
	@RequestMapping("/registrar")
    public String registrar(@RequestParam("un") String un, @RequestParam("cor") String cor, @RequestParam("pass") String pass,
    						@RequestParam("tel") int tel, @RequestParam("name") String na, @RequestParam("lna") String lna,
    						@RequestParam("cp") int cp, @RequestParam("ci") String ci, @RequestParam("pr") String pr,
    						@RequestParam("lat") String lat, @RequestParam("lon") String lon, @RequestParam("im") MultipartFile im
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
	        user = new usuario(un, cor, sb.toString(), tel, na, lna, cp, ci, pr);
	        user.setActivo(1);
	        
	        if ((lat != null) && (!lat.trim().equals("")) && (lon != null) && (!lon.trim().equals(""))){ 
	        	user.setLatitud(Float.parseFloat(lat));
	        	user.setLongitud(Float.parseFloat(lon));
	        }
	        if (!im.isEmpty()){ 
	        	int idIm = archiver.uploadFile(im);
	        	user.setArchivo(idIm);
	        }
	        else {
	        	user.setArchivo(0);
	        }
		}
		catch(Exception e){
			return e.getMessage();
		}
        
        repository.save(user);
        
        return "Ok";
    }
	
	@RequestMapping("/actualizarUsuario")
    public String actualizarUsuario(@RequestParam("un") String un,
    						@RequestParam("tel") int tel, @RequestParam("name") String na, @RequestParam("lna") String lna,
    						@RequestParam("cp") int cp, @RequestParam("ci") String ci, @RequestParam("pr") String pr,
    						@RequestParam("lat") String lat, @RequestParam("lon") String lon, @RequestParam("im") MultipartFile im
    						){
		
		if(!repository.existsBynombreusuario(un)) {
			return "El nombre de usuario no exite.";
		}
		
		usuario user = repository.findBynombreusuario(un).get();
		
		try {
			user.setTelefono(tel);
			user.setNombre(na);
	        user.setApellidos(lna);
	        user.setProvincia(pr);
	        user.setCodigopostal(cp);
	        user.setCiudad(ci);
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
	
	@RequestMapping("/logear")
	public String logear(@RequestParam("un") String un, @RequestParam("pass") String pass) {
		Optional<usuario> aux = repository.findBynombreusuario(un);
		if(aux.isPresent()) {
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
	
}
