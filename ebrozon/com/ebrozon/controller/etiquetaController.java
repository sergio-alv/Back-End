package com.ebrozon.controller;

import java.util.List;
import java.util.Optional;
import java.util.Vector;

import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

import com.ebrozon.model.etiqueta;
import com.ebrozon.repository.etiquetaRepository;
import com.ebrozon.repository.usuarioRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="Tag Management System", description="Operations pertaining to tag in Tag Managament System ")
public class etiquetaController {
	@Autowired
    etiquetaRepository repository;
	
	@Autowired
    usuarioRepository repository_u;
	
	//Guarda una etiqueta recibiendo como parámetros obligatorios el nombre de la etiqueta, la fecha
	//de creación y el nombre del creador.
	//http://localhost:8080/guardar?un=karny2&pass=caca&cor=cececw@gmail.com&na=saul&lna=alarcon
	@ApiOperation(value = "Save a tag in the database", response = String.class)
	@CrossOrigin
	@RequestMapping("/guardarEtiqueta")
	public String guardarEtiqueta(@RequestParam("et") String tn, @RequestParam("un") String crt) {
		if (repository.existsBynombre(tn)) {
			return "{O:Ok}";
		}
		
		if (!repository_u.existsBynombreusuario(crt)) {
			return "{E:El usuario creador de la etiqueta no existe.}";
		}
		
		etiqueta t;
		try {
			t = new etiqueta(tn,crt);
			repository.save(t);
		}
		catch(Exception e){
			return "{E:Problema inesperado al guardar la etiqueta}";
		}
		
		return "{O:Ok}";
	}
	
	public String asignarEtiqueta(int nv, String et) {
		try {
			repository.etiquetaApareceEnVenta(nv, et);
			return "{O:Ok}";
		}
		catch(Exception e) {
			return "{E:Ya existía esa relación.}";
		}
	}
	
	//Recupera la información de una etiqueta dado el nombre
	@ApiOperation(value = "Bring a tag from the database", response = etiqueta.class)
	@CrossOrigin
	@Produces("application/json")
	@RequestMapping("/recuperarEtiqueta")
	public etiqueta recuperarEtiqueta(@RequestParam("et") String tn) {
		Optional<etiqueta> aux = repository.findBynombre(tn);
		if (aux.isPresent()) {
			etiqueta t = aux.get();
			return t;
		}
		else {
			return null;
		}
	}
	
	//Actualiza la información de la etiqueta recibiendo como parámetros obligatorios
	//la nueva fecha de creación y el nombre del nuevo creador
	@ApiOperation(value = "Update a tag in the database", response = String.class)
	@CrossOrigin
	@RequestMapping("/actualizarEtiqueta")
	public String actualizarEtiqueta(@RequestParam("tn") String tn, @RequestParam("cd") Date cd, @RequestParam("crt") String crt) {
		if (!repository.existsBynombre(tn)) {
			return "La etiqueta no existe";
		}
		
		if (!repository_u.existsBynombreusuario(crt)) {
			return "El creador de la etiqueta no existe";
		}
		
		etiqueta t = repository.findBynombre(tn).get();
		
		try {
			t.setFechacreacion(cd);
			t.setCreador(crt);
		}
		catch(Exception e){
			return e.getMessage();
		}
		
		repository.save(t);
		
		return "Ok";
	}
	
	public void borrarEtiquetasVenta(int nv) {
		repository.borrarEtiquetasVenta(nv);
	}
	
	public List<Integer> ventasConEtiquetas(Vector ets) {
		return repository.ventasConEtiquetas((String[]) ets.toArray(new String[0]));
	}
	
	public List<Integer> ventasConEtiquetasMinDos(Vector ets) {
		return repository.ventasConEtiquetasMinDos((String[]) ets.toArray(new String[0]));
	}
	
	public List<Integer> ventasConEtiquetasOrdenadasPorCoincidencias(Vector ets) {
		String[] p = (String[]) ets.toArray(new String[0]);
		return repository.ventasConEtiquetasOrdenadasPorCoincidencias((String[]) ets.toArray(new String[0]));
	}
}