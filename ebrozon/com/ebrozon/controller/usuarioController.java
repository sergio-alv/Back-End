package com.ebrozon.controller;

import java.util.Date;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value="User Management System", description="Operations pertaining to user in User Managament System ")
public class usuarioController {
	@Autowired
    usuarioRepository repository;
	
	@Autowired
    archivoController archiver;
	
	@Autowired
	ventaController venter;
	
	@Autowired
	usuarioverantController usuarioveranter;
	
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
		   String mensaje = "Muchas gracias por registrarse en Ebrozon, le damos la bienvenida " + us + ".<br/>"
				   			+ "Para completar el registro, por favor haga click sobre el siguiente enlace:<br/><br/>"
				   			+ "http://localhost:8080/aceptarRegistro?un=" + us + "&ver=" + ver
				   			+ "<br/><br/>Si usted no sabe nada acerca de este registro, para cancelar el registro"
				   			+ " haga click sobre el siguiente enlace:<br/><br/>"
				   			+ "http://localhost:8080/rechazarRegistro?un=" + us + "&ver=" + ver
				   			+ "<br/><br/>Gracias y saludos";
		   msg.setContent(mensaje, "text/html; charset=utf-8");
		   msg.setSentDate(new Date());
		   Transport.send(msg);   
		}
	
	//Registra a un usuario recibiendo como parámetros obligatorios el nombre de usuario, el correo
	//la contraseña, el nombre y los apellidos, y siendo opcionales el teléfono, el código postal
	//la ciudad, la provincia, latitud y longitud, y la imagen de perfil.
	//localhost:8080/registrar?un=karny2&pass=caca&cor=cececw@gmail.com&na=saul&lna=alarcon
	@ApiOperation(value = "Register a user, returns {O:Ok} if ok or error message if not ok", response = String.class)
	@CrossOrigin
	@RequestMapping("/registrar")
    public String registrar(@ApiParam(value = "username", required = false) @RequestParam("un") String un, 
    		@ApiParam(value = "email", required = false) @RequestParam("cor") String cor, 
    		@ApiParam(value = "password", required = false) @RequestParam("pass") String pass,
    		@ApiParam(value = "telephone number", required = false) @RequestParam(value = "tel", required=false) Integer tel, 
    		@ApiParam(value = "name", required = false) @RequestParam("na") String na, 
    		@ApiParam(value = "family name", required = false) @RequestParam("lna") String lna,
    		@ApiParam(value = "postal code", required = false) @RequestParam(value = "cp", required=false) Integer cp, 
    		@ApiParam(value = "city", required = false) @RequestParam(value = "ci", required=false) String ci, 
    		@ApiParam(value = "province", required = false) @RequestParam(value = "pr", required=false) String pr,
    		@ApiParam(value = "latitude", required = false) @RequestParam(value = "lat", required=false) Float lat, 
    		@ApiParam(value = "longitude", required = false) @RequestParam(value = "lon", required=false) Float lon,
    		@ApiParam(value = "image", required = false) @RequestParam(value = "im", required=false) MultipartFile im){
		
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
			else{
				user.setCiudad(pr);
			}
	        
	        if ((pr != null) && (!pr.trim().equals(""))){ 
	        	user.setProvincia(pr);
	        }
	        
	        if ((lat != null) && (lon != null)){ 
	        	user.setLatitud(lat);
	        	user.setLongitud(lon);
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
	@ApiOperation(value = "Update a user, returns {O:Ok} if ok or error message if not ok", response = String.class)
	@CrossOrigin
	@RequestMapping("/actualizarUsuario")
    public String actualizarUsuario(@ApiParam(value = "username", required = false) @RequestParam("un") String un,
    		@ApiParam(value = "telephone number", required = false) @RequestParam(value = "tel", required=false) Integer tel, 
    		@ApiParam(value = "name", required = false) @RequestParam(value = "na", required=false) String na,
    		@ApiParam(value = "family name", required = false) @RequestParam(value = "lna", required=false) String lna,
    		@ApiParam(value = "postal code", required = false) @RequestParam(value = "cp", required=false) Integer cp, 
    		@ApiParam(value = "city", required = false) @RequestParam(value = "ci", required=false) String ci, 
    		@ApiParam(value = "province", required = false) @RequestParam(value = "pr", required=false) String pr,
    		@ApiParam(value = "latitude", required = false) @RequestParam(value = "lat", required=false) Float lat, 
    		@ApiParam(value = "longitude", required = false) @RequestParam(value = "lon", required=false) Float lon, 
    		@ApiParam(value = "image", required = false) @RequestParam(value = "im", required=false) String im){
		
		if(!repository.existsBynombreusuario(un)) {
			return "{E:El nombre de usuario no exite.}";
		}
		
		usuario user = repository.findBynombreusuario(un).get();
		usuarioveranter.guardar(user);
		
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
	        if (tel != null && tel == -1){ 
	        	user.setTelefono(0);
	        }
	        if (cp != null){ 
	        	user.setCodigopostal(cp);
	        }
	        if (cp != null && cp == -1){ 
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
			else{
				if ((pr != null) && (!pr.trim().equals(""))) {
					String e = venter.actualizarCiudadVentasUsuario(un, pr);
		        	if(!e.equals("{O:Ok}")) {
		        		return "{E:Error al actualizar la ciudad.}";
		        	}
		        	user.setCiudad(pr);
				}
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
	        
	        if ((lat != null) && (lon != null)){ 
	        	user.setLatitud(lat);
	        	user.setLongitud(lon);
	        	venter.actualizarLatLonVentasUsuario(un,lat,lon);
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
	
	@ApiOperation(value = "Change password, returns {O:Ok} if ok or error message if not ok", response = String.class)
	@CrossOrigin
	@RequestMapping("/cambiarContrasena")
	public String cambiarContrasena(@ApiParam(value = "username", required = false) @RequestParam("un") String un, 
			@ApiParam(value = "old password", required = false) @RequestParam("oldpass") String oldpass, 
			@ApiParam(value = "new password", required = false) @RequestParam("newpass") String newpass) {
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
			repository.save(us);
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
	@ApiOperation(value = "Login, returns {O:Ok} if ok or error massage if not ok", response = String.class)
	@CrossOrigin
	@RequestMapping("/logear")
	public String logear(@ApiParam(value = "username", required = false) @RequestParam("un") String un, 
			@ApiParam(value = "password", required = false) @RequestParam("pass") String pass) {
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
	
	@ApiOperation(value = "Accept registration, returns {O:Registro finalizado con exito} if ok or error message if not ok", response = String.class)
	@CrossOrigin
	@RequestMapping("/aceptarRegistro")
	public String aceptarRegistro(@ApiParam(value = "username", required = false) @RequestParam("un") String un, 
			@ApiParam(value = "verify password", required = false) @RequestParam("ver") String ver) {
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
	
	@ApiOperation(value = "Reject registration, returns {O:Registro cancelado con exito} if ok or error message if not ok", response = String.class)
	@CrossOrigin
	@RequestMapping("/rechazarRegistro")
	public String rechazarRegistro(@ApiParam(value = "username", required = false) @RequestParam("un") String un, 
			@ApiParam(value = "verify password", required = false) @RequestParam("ver") String ver) {
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
	@ApiOperation(value = "Recover user, returns user", response = usuario.class)
	@CrossOrigin
	@RequestMapping("/recuperarUsuario")
	@Produces("application/json")
	public Optional<usuario> recuperarUsuario(@ApiParam(value = "username", required = false) @RequestParam("un") String un) {
		Optional<usuario> aux = repository.findBynombreusuario(un);
		if(aux.isPresent()) {
			aux.get().setUrlArchivo(archiver.loadFileUrl(aux.get().getArchivo()));
		}
		return aux;
	}
	
	//Desactiva la cuenta del usuario identificado por el nombre de usuario dado
	@ApiOperation(value = "Ban a user, returns {O:Ok} if ok or error message if not ok", response = String.class)
	@CrossOrigin
	@RequestMapping("/banearUsuario")
	public String banearUsuario(@ApiParam(value = "username", required = false) @RequestParam("un") String un) {
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
	@ApiOperation(value = "Allow a user, returns {O:Ok} if ok or error message if not ok", response = String.class)
	@CrossOrigin
	@RequestMapping("/desbanearUsuario")
	public String desbanearUsuario(@ApiParam(value = "username", required = false) @RequestParam("un") String un) {
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
	
	@ApiOperation(value = "Prove a user existance, return true if exists false otherwise", response = boolean.class)
	@CrossOrigin
	@RequestMapping("/existeUsuario")
	boolean existeUsuario(@ApiParam(value = "username", required = false) @RequestParam("un") String un) {
		return repository.existsBynombreusuario(un);
	}
	
	String actualizarEstrellasUsuario(String un, double estrellas) {
		repository.actualizarEstrellas(un, estrellas);
		return "{O:Ok}";
	}
	
	String randomPass() {
		String cars = "b0320djq2j3gnn3as435widhb08872q3bcj232hfliw83blasetailonhgha3";
		Random r = new Random();
		int rI = 0;
		String pass = "";
		for(int i = 0; i < 16; ++i) {
			rI = r.nextInt((99999))%cars.length();
			pass += cars.charAt(rI);
		}
		return pass;
	}
	
	private void sendmail2(String us, String pass, String cor) throws AddressException, MessagingException, IOException {
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
		   msg.setSubject("Recuperación de contraseña en Ebrozon");
		   String mensaje = "Usted, " + us + ", ha solicitado recuperar su contraseña.<br/>"
				   			+ "En este momento se le ha cambiado a contraseña a la siguiente:<br/><br/>"
				   			+ pass
				   			+ "<br/><br/>Para completar el proceso de recuperación vaya a su perfil y cambie su contraseña."
				   			+ "<br/>Gracias y saludos";
		   msg.setContent(mensaje, "text/html; charset=utf-8");
		   msg.setSentDate(new Date());
		   Transport.send(msg);   
		}
	
	@ApiOperation(value = "Recover password, returns {O:Ok} if ok or error message if not ok", response = String.class)
	@CrossOrigin
	@RequestMapping("/recuperarContrasena")
	public String cambiarContrasenaRec(@ApiParam(value = "username", required = false) @RequestParam("un") String un) {
		Optional<usuario> aux = repository.findBynombreusuario(un);
		String newpass;
		String oldpass;
		if(aux.isPresent()) {
			if(aux.get().getActivo() == 0) {
				return "{E:La cuenta está deshabilitada.}";
			}
			MessageDigest md;
	    	StringBuffer sb = new StringBuffer();
		    try {
		    	newpass = randomPass();
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
		    oldpass = us.getContrasena();
		    us.setContrasena(sb.toString());
		    repository.save(us);
		    try {
		    	//sendmail2(us.getNombreusuario(),newpass,us.getCorreo());
		    }
		    catch(Exception e) {
		    	us.setContrasena(oldpass);
		    	repository.save(us);
		    	return "{E:Ha habido un problema durante el cambio de contraseña. Se mantendrá la anterior.}";
		    }
		    return "{O:Ok}";
		}
		else {
			return "{E:El nombre usuario no existe.}";
		}
	}
}