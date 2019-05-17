package com.ebrozon.controller;

import java.util.Date;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Produces;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;

import com.ebrozon.model.usuario;
import com.ebrozon.repository.usuarioRepository;

@RestController
public class usuarioController {
	@Autowired
    usuarioRepository repository;
	
	@Autowired
    archivoController archiver;
	
	@Autowired
	ventaController venter;
	
	private void sendmail(String us, String ver, String cor) throws AddressException, MessagingException, IOException {
		   Properties props = new Properties();
		   props.put("mail.smtp.auth", "true");
		   props.put("mail.smtp.starttls.enable", "true");
		   props.put("mail.smtp.host", "smtp.gmail.com");
		   props.put("mail.smtp.port", "587");
		   
		   Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		      protected PasswordAuthentication getPasswordAuthentication() {
		         return new PasswordAuthentication("karny.sac@gmail.com", "199819981998s");
		      }
		   });
		   Message msg = new MimeMessage(session);
		   msg.setFrom(new InternetAddress("karny.sac@gmail.com", false));

		   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(cor));
		   msg.setSubject("Registro en Ebrozon");
		   String mensaje = "Muchas gracias por registrarse en Ebrozon, le damos la bienvenida " + us + ".\n"
				   			+ "Para completar el registro, por favor haga click sobre el siguiente enlace:\n\n"
				   			+ "http://localhost:8080/aceptarRegistro?un=" + us + "&ver=" + ver
				   			+ "\nSi usted no sabe nada acerca de este registro, para cancelar el registro"
				   			+ "haga click sobre el siguiente enlace\n\n"
				   			+ "http://localhost:8080/rechazarRegistro?un=" + us + "&ver=" + ver
				   			+ "\nGracias y saludos";
		   msg.setContent(mensaje, "text/html; charset=utf-8");
		   msg.setSentDate(new Date());
		   Transport.send(msg);   
		}
	
	//Registra a un usuario recibiendo como parámetros obligatorios el nombre de usuario, el correo
	//la contraseña, el nombre y los apellidos, y siendo opcionales el teléfono, el código postal
	//la ciudad, la provincia, latitud y longitud, y la imagen de perfil.
	//localhost:8080/registrar?un=karny2&pass=caca&cor=cececw@gmail.com&na=saul&lna=alarcon
	@CrossOrigin
	@RequestMapping("/registrar")
    public String registrar(@RequestParam("un") String un, @RequestParam("cor") String cor, @RequestParam("pass") String pass,
    						@RequestParam(value = "tel", required=false) Integer tel, @RequestParam("na") String na, @RequestParam("lna") String lna,
    						@RequestParam(value = "cp", required=false) Integer cp, @RequestParam(value = "ci", required=false) String ci, @RequestParam(value = "pr", required=false) String pr,
    						@RequestParam(value = "lat", required=false) String lat, @RequestParam(value = "lon", required=false) String lon, @RequestParam(value = "im", required=false) MultipartFile im
    						){
		
		if(repository.existsBynombreusuario(un)) {
			return "{E:El nombre de usuario no está disponible.}";
		}
		if(repository.existsBycorreo(cor)) {
			return "{E:El correo electrónico no está disponible.}";
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
    		return "{E:Ha habido un problema durante el registro.}";
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
	        	user.setArchivo(1);
	        }
	        
	        user.setActivo(1);
	        //sendmail(user.getNombreusuario(),user.getContrasena(), user.getCorreo());
	        repository.save(user);
		}
		catch(Exception e) {
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
        return "{O:Ok}";
    }
	
	//Actualiza la información de un usuario recibiendo como parámetros obligatorios el nombre de usuario, el correo
	//la contraseña, el nombre y los apellidos, y siendo opcionales el teléfono, el código postal
	//la ciudad, la provincia, latitud y longitud, y la imagen de perfil.
	@CrossOrigin
	@RequestMapping("/actualizarUsuario")
    public String actualizarUsuario(@RequestParam("un") String un,
			@RequestParam(value = "tel", required=false) Integer tel, @RequestParam(value = "na", required=false) String na, @RequestParam(value = "lna", required=false) String lna,
			@RequestParam(value = "cp", required=false) Integer cp, @RequestParam(value = "ci", required=false) String ci, @RequestParam(value = "pr", required=false) String pr,
			@RequestParam(value = "lat", required=false) String lat, @RequestParam(value = "lon", required=false) String lon, @RequestParam(value = "im", required=false) String im
			){
		
		if(!repository.existsBynombreusuario(un)) {
			return "{E:El nombre de usuario no exite.}";
		}
		
		usuario user = repository.findBynombreusuario(un).get();
		
		try {
			
			if (na != null){ 
				user.setNombre(na);
	        }
			
			if (lna != null){ 
				user.setApellidos(lna);
	        }
			
	        if (tel != null){ 
	        	user.setTelefono(tel);
	        }
	        if (tel == -1){ 
	        	user.setTelefono(0);
	        }
	        if (cp != null){ 
	        	user.setCodigopostal(cp);
	        }
	        if (cp == -1){ 
	        	user.setCodigopostal(0);
	        }
	        if ((ci != null) && (!ci.trim().equals(""))){ 
	        	if(!ci.equals(user.getCiudad())) {
	        		String e = venter.actualizarCiudadVentasUsuario(un, ci);
	        		if(!e.equals("{O:Ok}")) {
	        			return "{E:Error al actualizar la ciudad.}";
	        		}
	        	}
	        	user.setCiudad(ci);
	        }
	        
	        if ((pr != null) && (!pr.trim().equals(""))){ 
	        	if(!pr.equals(user.getProvincia())) {
	        		String e = venter.actualizarProvinciaVentasUsuario(un, pr);
	        		if(!e.equals("{O:Ok}")) {
	        			return "{E:Error al actualizar la provincia.}";
	        		}
	        	}
	        	if(user.getCiudad() == null || user.getCiudad().equals("")) {
	        		String e = venter.actualizarCiudadVentasUsuario(un, pr);
	        		if(!e.equals("{O:Ok}")) {
	        			return "{E:Error al actualizar la ciudad.}";
	        		}
	        		
	        	}
	        	user.setProvincia(pr);
	        }
	        
	        if ((lat != null) && (!lat.trim().equals("")) && (lon != null) && (!lon.trim().equals(""))){ 
	        	user.setLatitud(Float.parseFloat(lat));
	        	user.setLongitud(Float.parseFloat(lon));
	        }
	        if (im != null && !im.isEmpty()){ 
	        	int idIm = archiver.uploadArchivoTemp(im);
	        	if(idIm == -1) {
	        		return "{E:Error al actualizar la imagen.}";
	        	}
	        	user.setArchivo(idIm);
	        }
	        repository.save(user);
		}
		catch(Exception e){
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
        return "{O:Ok}";
    }
	
	@CrossOrigin
	@RequestMapping("/cambiarContrasena")
	public String cambiarContrasena(@RequestParam("un") String un, @RequestParam("oldpass") String oldpass, @RequestParam("newpass") String newpass) {
		Optional<usuario> aux = repository.findBynombreusuario(un);
		if(aux.isPresent()) {
			if(aux.get().getActivo() == 0) {
				return "{E:La cuenta está deshabilitada.}";
			}
			MessageDigest md;
	    	StringBuffer sb = new StringBuffer();
		    try {
		    	md = MessageDigest.getInstance("MD5");
		    	md.update(oldpass.trim().getBytes());
				byte[] digest = md.digest();
				sb = new StringBuffer();
				for (byte b : digest) {
					sb.append(String.format("%02x", b & 0xff));
				}
	    	}
	    	catch(Exception e) {
	    		return "{E:Ha habido un problema durante el cambio de contraseña. Se mantendrá la anterior.}";
	    	}
		    
		    if(sb.toString().equals(aux.get().getContrasena())) {
		    	try {
			    	md = MessageDigest.getInstance("MD5");
			    	md.update(newpass.trim().getBytes());
					byte[] digest = md.digest();
					sb = new StringBuffer();
					for (byte b : digest) {
						sb.append(String.format("%02x", b & 0xff));
					}
		    	}
		    	catch(Exception e) {
		    		return "{E:Ha habido un problema durante el cambio de contraseña. Se mantendrá la anterior.}";
		    	}
		    	usuario us = aux.get();
		    	us.setContrasena(sb.toString());
		    	return "{O:Ok}";
		    }
		    else {
		    	return "{E:La contraseña es incorrecta.}";
		    }
		}
		else {
			return "{E:El nombre usuario no existe.}";
		}
	}
	
	//Comprueba la información del usuario para logearse, recibiendo como parámetros su
	//nombre de usuario y su contraseña
	//http://localhost:8080/logear?un=karny1&pass=caca
	@CrossOrigin
	@RequestMapping("/logear")
	public String logear(@RequestParam("un") String un, @RequestParam("pass") String pass) {
		Optional<usuario> aux = repository.findBynombreusuario(un);
		if(aux.isPresent()) {
			if(aux.get().getActivo() == 0) {
				return "{E:La cuenta está deshabilitada.}";
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
	    		return "{E:Ha habido un problema durante el login.}";
	    	}
		    
		    if(sb.toString().equals(aux.get().getContrasena())) {
		    	repository.registrarLogin(un);
		    	return "{O:Ok}";
		    }
		    else {
		    	return "{E:La contraseña es incorrecta.}";
		    }
		}
		else {
			return "{E:El nombre usuario no existe.}";
		}
	}
	
	@CrossOrigin
	@RequestMapping("/aceptarRegistro")
	public String aceptarRegistro(@RequestParam("un") String un, @RequestParam("ver") String ver) {
		Optional<usuario> aux = repository.findBynombreusuario(un);
		if(aux.isPresent()) {
			if(ver.equals(aux.get().getContrasena())) {
				aux.get().setActivo(1);
				repository.save(aux.get());
				return "{O:Registro finalizado con éxito}";
			}
			else {
				return "{E:Por favor vuelva a registrarse.}";
			}
		}
		else {
			return "{E:El nombre usuario no existe.}";
		}
	}
	
	@CrossOrigin
	@RequestMapping("/rechazarRegistro")
	public String rechazarRegistro(@RequestParam("un") String un, @RequestParam("ver") String ver) {
		Optional<usuario> aux = repository.findBynombreusuario(un);
		if(aux.isPresent()) {
			if(ver.equals(aux.get().getContrasena())) {
				repository.delete(aux.get());
				return "{O:Registro cancelado con éxito}";
			}
			else {
				return "{E:Por favor vuelva a registrarse.}";
			}
		}
		else {
			return "{E:El nombre usuario no existe.}";
		}
	}
	
	//Recupera la información de un usuario dado su nombre de usuario
	@CrossOrigin
	@RequestMapping("/recuperarUsuario")
	@Produces("application/json")
	public Optional<usuario> recuperarUsuario(@RequestParam("un") String un) {
		Optional<usuario> aux = repository.findBynombreusuario(un);
		if(aux.isPresent()) {
			aux.get().setUrlArchivo(archiver.loadFileUrl(aux.get().getArchivo()));
			aux.get().setContrasena("cwecasdvev");
		}
		return aux;
	}
	
	//Desactiva la cuenta del usuario identificado por el nombre de usuario dado
	@CrossOrigin
	@RequestMapping("/banearUsuario")
	public String banearUsuario(@RequestParam("un") String un) {
		Optional<usuario> aux = repository.findBynombreusuario(un);
		if(aux.isPresent()) {
			usuario user = aux.get();
			user.setActivo(0);
			repository.save(user);
			return "{O:Ok}";
		}
		else {
			return "{E:No se ha encontrado el usuario.}";
		}
	}
	
	//Activa la cuenta del usuario identificado por el nombre de usuario dado
	@CrossOrigin
	@RequestMapping("/desbanearUsuario")
	public String desbanearUsuario(@RequestParam("un") String un) {
		Optional<usuario> aux = repository.findBynombreusuario(un);
		if(aux.isPresent()) {
			usuario user = aux.get();
			user.setActivo(1);
			repository.save(user);
			return "{O:Ok}";
		}
		else {
			return "{E:No se ha encontrado el usuario.}";
		}
	}
	
	@CrossOrigin
	@RequestMapping("/existeUsuario")
	boolean existeUsuario(@RequestParam("un") String un) {
		return repository.existsBynombreusuario(un);
	}
	
	String actualizarEstrellasUsuario(String un, double estrellas) {
		repository.actualizarEstrellas(un, estrellas);
		return "{O:Ok}";
	}
}
