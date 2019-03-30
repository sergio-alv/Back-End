package com.ebrozon.controller;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

import com.ebrozon.model.venta;
import com.ebrozon.model.usuario;
import com.ebrozon.repository.ventaRepository;

@RestController
public class ventaController {
	
	@Autowired
    ventaRepository repository;
	
	@Autowired
    archivoController archiver;
	
	
	//Publica una venta recibiendo como parámetros nombre de usuario, título del producto, descripción
	//y precio, siendo opcionales los archivos
	@RequestMapping("/publicarVenta")
	public String publicarVenta(@RequestParam("un") String un, @RequestParam("prod") String prod, @RequestParam("desc") String desc,
			@RequestParam("pre") double pre, @RequestParam(value = "arc", required=false) MultipartFile arc) {
		int idIm = -1;
		venta vent;
		try {
			vent = new venta(un,prod, desc, pre, 0, 1);
			if(arc != null) {
				vent.setTienearchivo(1);
				idIm = archiver.uploadFile(arc);
				repository. archivoApareceEnVenta(idIm, un, vent.getFechainicio());
				repository.save(vent);
			}
		}
		catch(Exception e) {
			archiver.deleteFile(idIm);
			return "Ha habido un problema durante la publicación del producto.";
		}
		return "Ok";
	}
}
