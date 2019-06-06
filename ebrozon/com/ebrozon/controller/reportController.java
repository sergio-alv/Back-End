package com.ebrozon.controller;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebrozon.model.report;
import com.ebrozon.repository.opinionRepository;
import com.ebrozon.repository.reportRepository;
import com.ebrozon.repository.ventaRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value="Report Management System", description="Operations pertaining to report in Report Managament System ")
public class reportController {
	
	@Autowired
    reportRepository repository;
	
	@Autowired
    usuarioController userer;
	
	@Autowired
	ventaRepository venter;
	
	@Autowired
	opinionRepository opiner;
	
	@ApiOperation(value = "Send a report, returns {O:Ok} if ok or error message if not ok", response = String.class)
	@CrossOrigin
	@RequestMapping("/mandarReport")
	String mandarReport(@ApiParam(value = "report's emitter", required = false) @RequestParam("em") String em, 
			@ApiParam(value = "report's reciever", required = false) @RequestParam("re") String re, 
			@ApiParam(value = "content", required = false) @RequestParam("con") String con,
			@ApiParam(value = "sale's id", required = false) @RequestParam(value = "nv", required=false) Integer nv, 
			@ApiParam(value = "reason", required = false) @RequestParam("mov") String mov) {
		if(!userer.existeUsuario(em)) {return "{E:No existe el usuario emisor}";}
		if(!userer.existeUsuario(re)) {return "{E:No existe el usuario receptor}";}
		if(nv != null && !venter.existsByidentificador(nv)) {return "{E:No existe la venta}";}
		int id = 1;
		Optional<Integer> idAux = repository.lastId();
		if(idAux.isPresent()) {
			id = idAux.get()+1;
		}
		try {
			report m;
			if(nv != null) {
				 m = new report(em,re,con,nv, mov);
			}
			else {
				m = new report(em,re,con, mov);
			}
			m.setIdentificador(id);
			repository.save(m);
			comprobarBaneoUsuario(re);
		}catch(Exception e) {
			return "{E:Problema al enviar el report}";
		}
		return "{O:Ok}";
	}
	
	@ApiOperation(value = "List all reports recieved, returns list of reports", response = List.class)
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarReportesRecibidos")
	List<report> listarReportesRecibidos(@ApiParam(value = "username", required = false) @RequestParam("un") String un){
		List<report> list = repository.findByreceptorOrderByIdentificadorDesc(un);
		return list;
	}
	
	@ApiOperation(value = "List all reports made, returns list of reports", response = List.class)
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/listarReportesHechos")
	List<report> listarReportesHechos(@ApiParam(value = "username", required = false) @RequestParam("un") String un){
		List<report> list = repository.findByemisorOrderByIdentificadorDesc(un);
		return list;
	}
	
	@ApiOperation(value = "Number of reports recieved, returns number of reports", response = int.class)
	@CrossOrigin
	@RequestMapping("/numeroReportesRecibidos")
	int numeroReportesRecibidos(@ApiParam(value = "username", required = false) @RequestParam("un") String un) {
		return repository.numeroReportesRecibidos(un);
	}
	
	private void comprobarBaneoUsuario(String un) {
		double rep = repository.numeroReportesRecibidos(un);
		if(rep >= 7) {
			double val = opiner.numeroOpinionesRecibidas(un);
			if(rep/(rep+val) > 0.5) {
				userer.banearUsuario(un);
			}
		}
	}
	
}
