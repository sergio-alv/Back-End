package com.ebrozon.controller;

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

import com.ebrozon.model.archivo;
import com.ebrozon.model.usuario;
import com.ebrozon.repository.archivoRepository;

@RestController
public class archivoController {
	
	@Autowired
    archivoRepository repository;
	
	private String upload_folder = ".//src//main//resources//files";
	
	@RequestMapping("/uploadFile")
	public int uploadFile(@RequestParam("file") MultipartFile file) {
		if(!file.isEmpty()) {
			try {
				byte[] array = new byte[10];
				new Random().nextBytes(array);
				String fileName = new String(array, Charset.forName("UTF-8")) + "-" + file.getOriginalFilename();
				saveFile(file, fileName);
				archivo f = new archivo(upload_folder + fileName, 0);
				repository.save(f);
				return repository.findByurl(upload_folder + fileName).get().getIdentificador();
			}
			catch(IOException e) {
				return -1;
			}
		}
		return -1;
	}
	
	@RequestMapping("/loadFile")
	public String loadFile(@RequestParam("identificador") int id) {
		Optional<archivo> aux = repository.findByidentificador(id);
		if(aux.isPresent()) {
			return aux.get().getUrl();
		}
		return "Ha habido un problema con el archivo";
	}
	
	@RequestMapping("/deleteFile")
	public String deleteFile(int id) {
		Optional<archivo> aux = repository.findByidentificador(id);
		try {
			archivo arc = aux.get();
			arc.setBorrada(1);
			repository.save(arc);
		}
		catch(Exception e) {
			return "Ha habido un problema al borrar el archivo.";
		}
		return "Ok";
	}
	
	private void saveFile(MultipartFile file, String filename) throws IOException {
		if(!file.isEmpty()) {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(upload_folder + filename);
			Files.write(path, bytes);
		}
	}
}
