package com.ebrozon.controller;

import java.util.Optional;
import java.nio.file.Paths;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.ebrozon.model.archivo;
import com.ebrozon.repository.archivoRepository;

@RestController
public class archivoController {
	
	@Autowired
    archivoRepository repository;
	
	private String upload_folder = ".//src//main//resources//files//";
	
	@RequestMapping("/uploadFile")
	public int uploadFile(@RequestParam("file") MultipartFile file) {
		if(!file.isEmpty()) {
			try {
				int id = 1;
				Optional<Integer> idAux = repository.lastId();
				if(idAux.isPresent()) {
					id = idAux.get()+1;
				}
				byte[] array = new byte[10];
				new Random().nextBytes(array);
				String fileName = Integer.toString(id)  + "-" + file.getOriginalFilename();
				saveFile(file, fileName);
				archivo f = new archivo(upload_folder + fileName, 0);
				f.setIdentificador(id);
				repository.save(f);
				return repository.findByurl(upload_folder + fileName).get().getIdentificador();
			}
			catch(IOException e) {
				return -1;
			}
		}
		return -1;
	}
	
	@RequestMapping("/loadFileUrl")
	public String loadFileUrl(@RequestParam("id") int id) {
		Optional<archivo> aux = repository.findByidentificador(id);
		if(aux.isPresent()) {
			return aux.get().getUrl();
		}
		return "{E:Ha habido un problema con el archivo.}";
	}
	
	@RequestMapping("/loadFile")
	public File loadFile(@RequestParam("id") int id) {
		Optional<archivo> aux = repository.findByidentificador(id);
		if(aux.isPresent()) {
			try {
				File f = Paths.get(aux.get().getUrl()).toFile();
				long x = f.length();
				return Paths.get(aux.get().getUrl()).toFile();
			}
			catch(Exception e){return null;}
		}
		return null;
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
			return "{E:Ha habido un problema al borrar el archivo.}";
		}
		return "{O:Ok}";
	}
	
	private void saveFile(MultipartFile file, String filename) throws IOException {
		if(!file.isEmpty()) {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(upload_folder + filename);
			Files.write(path, bytes);
		}
	}
}
