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

import com.ebrozon.model.usuario;
import com.ebrozon.model.usuarioverant;
import com.ebrozon.repository.usuarioverantRepository;

import io.swagger.annotations.Api;

@RestController
@Api(value="Historical user information Management System", description="Operations pertaining to historical user information in Historical user information Managament System ")
public class usuarioverantController {
	@Autowired
    usuarioverantRepository repository;
	
    public void guardar(usuario u){
		usuarioverant uv = new usuarioverant(u);
		repository.save(uv);	
    }
    
}