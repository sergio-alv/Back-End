package com.ebrozon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebrozon.model.Customer;
import com.ebrozon.model.usuario;
import com.ebrozon.repository.usuarioRepository;

@RestController
public class usuarioController {
	@Autowired
    usuarioRepository repository;
	
	@RequestMapping("/registrar")
    public String registrar(){
        String result = "";
        usuario aux = new usuario("admin", "adminf@admin.com", "adminadmin", 999999999, "admin", "admin admin", 99999, "adminlandia");
        
        	repository.save(aux);
        
        return result;
    }
}
