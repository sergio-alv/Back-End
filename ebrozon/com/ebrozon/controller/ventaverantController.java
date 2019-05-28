package com.ebrozon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ebrozon.model.venta;
import com.ebrozon.model.ventaverant;
import com.ebrozon.repository.ventaverantRepository;

import io.swagger.annotations.Api;

@RestController
@Api(value="Historical sales information Management System", description="Operations pertaining to historical sales information in Historical sales information Managament System ")
public class ventaverantController {
	@Autowired
    ventaverantRepository repository;
	
	public void guardar(venta v) {
		ventaverant vv = new ventaverant(v);
		repository.save(vv);
	}
}