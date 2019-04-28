package com.ebrozon.controller;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebrozon.model.opinion;
import com.ebrozon.repository.opinionRepository;

@RestController
public class opinionController {
	
	@Autowired
    opinionRepository repository;
	
	@Autowired
    usuarioController userer;
	
	@RequestMapping("/mandarOpinion")
	String mandarOpinion(@RequestParam("em") String em, @RequestParam("re") String re, @RequestParam("con") String con, @RequestParam("es") Double es) {
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
	
	@RequestMapping("/listarOpinionesRecibidas")
	@Produces("application/json")
	List<opinion> listarOpinionesRecibidas(@RequestParam("un") String un){
		List<opinion> list = repository.findByreceptorOrderByIdentificadorDesc(un);
		return list;
	}
	
	@RequestMapping("/listarOpinionesHechas")
	@Produces("application/json")
	List<opinion> listarOpinionesHechas(@RequestParam("un") String un){
		List<opinion> list = repository.findByemisorOrderByIdentificadorDesc(un);
		return list;
	}
	
}
