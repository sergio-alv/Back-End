package com.ebrozon.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebrozon.model.mensaje;
import com.ebrozon.model.usuario;
import com.ebrozon.repository.mensajeRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="Message Management System", description="Operations pertaining to message in Message Managament System ")
public class mensajeController {
	
	@Autowired
    mensajeRepository repository;
	
	@Autowired
    usuarioController userer;
	
	@ApiOperation(value = "Send a message to a user", response = String.class)
	@CrossOrigin
	@RequestMapping("/mandarMensaje")
	String mandarMensaje(@RequestParam("em") String em, @RequestParam("re") String re, @RequestParam("con") String con) {
		if(!userer.existeUsuario(em)) {return "{E:No existe el usuario emisor}";}
		if(!userer.existeUsuario(re)) {return "{E:No existe el usuario receptor}";}
		int id = 1;
		Optional<Integer> idAux = repository.lastId();
		if(idAux.isPresent()) {
			id = idAux.get()+1;
		}
		try {
			mensaje m = new mensaje(id,em,re,con);
			repository.save(m);
		}catch(Exception e) {
			return "{E:Problema al enviar el mensaje}";
		}
		return "{O:Ok}";
	}
	
	@ApiOperation(value = "List all chats of a user", response = List.class)
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarChats")
	List<mensaje> listarChats(@RequestParam("un") String un){
		List<String> aux = repository.usuariosChateados(un);
		List<mensaje> chats = new ArrayList<mensaje>();
		for(int i = 0; i < aux.size(); ++i) {
			chats.add(repository.ultimoMensaje(un, aux.get(i)).get());
		}
		return chats;
	}
	
	@ApiOperation(value = "Bring all messages of a chat from the database", response = List.class)
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/cargarChat")
	List<mensaje> cargarChat(@RequestParam("em") String em, @RequestParam("re") String re){
		List<mensaje> list = repository.findByemisorAndReceptorOrReceptorAndEmisorOrderByIdentificadorAsc(em,re, em, re);
		return list;
	}
	
	@ApiOperation(value = "Recieve a message", response = List.class)
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/recibirMensaje")
	List<mensaje> recibirMensaje(@RequestParam("lm") int lm, @RequestParam("em") String em, @RequestParam("re") String re){
		List<mensaje> list = repository.findByidentificadorGreaterThanAndEmisorAndReceptorOrIdentificadorGreaterThanAndEmisorAndReceptorOrderByIdentificadorAsc(lm, em,re,lm, re, em);
		return list;
	}
}
