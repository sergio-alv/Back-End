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
import io.swagger.annotations.ApiParam;

@RestController
@Api(value="Message Management System", description="Operations pertaining to message in Message Managament System ")
public class mensajeController {
	
	@Autowired
    mensajeRepository repository;
	
	@Autowired
    usuarioController userer;
	
	@ApiOperation(value = "Send a message to a user, returns {O:Ok} if ok or error message if not ok", response = String.class)
	@CrossOrigin
	@RequestMapping("/mandarMensaje")
	String mandarMensaje(@ApiParam(value = "message's emitter (username)", required = false) @RequestParam("em") String em, 
			@ApiParam(value = "message's reciever (username)", required = false) @RequestParam("re") String re, 
			@ApiParam(value = "message's content", required = false) @RequestParam("con") String con) {
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
	
	@ApiOperation(value = "List all chats of a user, returns list of messages", response = List.class)
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarChats")
	List<mensaje> listarChats(@ApiParam(value = "username", required = false) @RequestParam("un") String un){
		List<String> aux = repository.usuariosChateados(un);
		List<mensaje> chats = new ArrayList<mensaje>();
		for(int i = 0; i < aux.size(); ++i) {
			chats.add(repository.ultimoMensaje(un, aux.get(i)).get());
		}
		return chats;
	}
	
	@ApiOperation(value = "Bring all messages of a chat from the database, returns list of messages", response = List.class)
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/cargarChat")
	List<mensaje> cargarChat(@ApiParam(value = "messages emitter (username)", required = false) @RequestParam("em") String em,
			@ApiParam(value = "messages reciever (username)", required = false) @RequestParam("re") String re){
		List<mensaje> list = repository.findByemisorAndReceptorOrReceptorAndEmisorOrderByIdentificadorAsc(em,re, em, re);
		return list;
	}
	
	@ApiOperation(value = "Recieve a message, returns list of messages", response = List.class)
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/recibirMensaje")
	List<mensaje> recibirMensaje(@ApiParam(value = "last message's id", required = false) @RequestParam("lm") int lm, 
			@ApiParam(value = "messages emitter (username)", required = false) @RequestParam("em") String em, 
			@ApiParam(value = "messages reciever (username)", required = false) @RequestParam("re") String re){
		List<mensaje> list = repository.findByidentificadorGreaterThanAndEmisorAndReceptorOrIdentificadorGreaterThanAndEmisorAndReceptorOrderByIdentificadorAsc(lm, em,re,lm, re, em);
		return list;
	}
}
