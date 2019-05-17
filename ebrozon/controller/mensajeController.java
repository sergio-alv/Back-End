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

@RestController
public class mensajeController {
	
	@Autowired
    mensajeRepository repository;
	
	@Autowired
    usuarioController userer;
	
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
	
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarChats")
	List<mensaje> listarChats(@RequestParam("un") String un){
		List<String> aux = repository.usuariosChateados(un);
		List<usuario> list = new ArrayList<usuario>();
		for(int i = 0; i < aux.size(); ++i) {
			list.add(userer.recuperarUsuario(aux.get(i)).get());
		}
		List<mensaje> chats = new ArrayList<mensaje>();
		for(int i = 0; i < list.size(); ++i) {
			chats.add(repository.ultimoMensaje(un, list.get(i).getNombreusuario()).get());
		}
		return chats;
	}
	
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/cargarChat")
	List<mensaje> cargarChat(@RequestParam("em") String em, @RequestParam("re") String re){
		List<mensaje> list = repository.findByemisorAndReceptorOrReceptorAndEmisorOrderByIdentificadorAsc(em,re, em, re);
		return list;
	}
	
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/recibirMensaje")
	List<mensaje> recibirMensaje(@RequestParam("lm") int lm, @RequestParam("em") String em, @RequestParam("re") String re){
		List<mensaje> list = repository.findByidentificadorGreaterThanAndEmisorAndReceptorOrderByIdentificadorAsc(lm, em,re);
		return list;
	}
}
