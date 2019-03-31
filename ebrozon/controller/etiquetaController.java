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
import java.sql.Date;

import com.ebrozon.model.etiqueta;
import com.ebrozon.model.usuario;
import com.ebrozon.repository.etiquetaRepository;
import com.ebrozon.repository.usuarioRepository;

@RestController
public class etiquetaController {
	@Autowired
    etiquetaRepository repository;
	
	@Autowired
    usuarioRepository repository_u;
	
	//Guarda una etiqueta recibiendo como parámetros obligatorios el nombre de la etiqueta, la fecha
	//de creación y el nombre del creador.
	//http://localhost:8080/guardar?un=karny2&pass=caca&cor=cececw@gmail.com&na=saul&lna=alarcon
	@RequestMapping("/guardar")
	public String guardar(@RequestParam("tn") String tn, @RequestParam("cd") Date cd, @RequestParam("crt") String crt) {
		if (repository.existsBynombre(tn)) {
			return "Etiqueta ya guardada";
		}
		
		if (!repository_u.existsBynombreusuario(crt)) {
			return "El creador de la etiqueta no existe";
		}
		
		etiqueta t;
		try {
			t = new etiqueta(tn,cd,crt);
		}
		catch(Exception e){
			return e.getMessage();
		}
		
		repository.save(t);
		
		return "Ok";
	}
	
	//Recupera la información de una etiqueta dado el nombre
	@RequestMapping("/recuperarEtiqueta")
	public etiqueta recuperarEtiqueta(@RequestParam("tn") String tn) {
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
}