package com.ebrozon.controller;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebrozon.model.opinion;
import com.ebrozon.repository.opinionRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value="Opinion Management System", description="Operations pertaining to opinion in Opinion Managament System ")
public class opinionController {
	
	@Autowired
    opinionRepository repository;
	
	@Autowired
    usuarioController userer;
	
	@ApiOperation(value = "Send an opinion, returns {O:Ok} if ok or error message if not ok", response = String.class)
	@CrossOrigin
	@RequestMapping("/mandarOpinion")
	String mandarOpinion(@ApiParam(value = "opinion's emitter (username)", required = false) @RequestParam("em") String em,
			@ApiParam(value = "opinion's reciever (username)", required = false) @RequestParam("re") String re, 
			@ApiParam(value = "content", required = false)@RequestParam("con") String con,
			@ApiParam(value = "stars", required = false) @RequestParam("es") Double es) {
		if(!userer.existeUsuario(em)) {return "{E:No existe el usuario emisor}";}
		if(!userer.existeUsuario(re)) {return "{E:No existe el usuario receptor}";}
		int id = 1;
		Optional<Integer> idAux = repository.lastId();
		if(idAux.isPresent()) {
			id = idAux.get()+1;
		}
		try {
			opinion m = new opinion(id,em,re,con,es);
			repository.save(m);
			double estrellas = repository.mediaEstrellasUsuario(re);
			userer.actualizarEstrellasUsuario(re, estrellas);
		}catch(Exception e) {
			return "{E:Problema al enviar el opinion}";
		}
		return "{O:Ok}";
	}
	
	@ApiOperation(value = "List all opinions recieved, returns list of opinions", response = List.class)
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarOpinionesRecibidas")
	List<opinion> listarOpinionesRecibidas(@ApiParam(value = "username", required = false) @RequestParam("un") String un){
		List<opinion> list = repository.findByreceptorOrderByIdentificadorDesc(un);
		return list;
	}
	
	@ApiOperation(value = "List all opinions made by a user, returns list of opinions", response = List.class)
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarOpinionesHechas")
	List<opinion> listarOpinionesHechas(@ApiParam(value = "username", required = false) @RequestParam("un") String un){
		List<opinion> list = repository.findByemisorOrderByIdentificadorDesc(un);
		return list;
	}
	
	@ApiOperation(value = "Number of opinions recieved, returns number of opinions", response = int.class)
	@CrossOrigin
	@RequestMapping("/numeroOpinionesRecibidas")
	int numeroOpinionesRecibidas(@ApiParam(value = "username", required = false) @RequestParam("un") String un) {
		return repository.numeroOpinionesRecibidas(un);
	}
	
	@ApiOperation(value = "Number of opinions made, returns number of opinions", response = int.class)
	@CrossOrigin
	@RequestMapping("/numeroOpinionesRealizadas")
	int numeroOpinionesRealizadas(@ApiParam(value = "username", required = false) @RequestParam("un") String un) {
		return repository.numeroOpinionesRealizadas(un);
	}
}
