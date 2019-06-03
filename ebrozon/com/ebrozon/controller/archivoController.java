package com.ebrozon.controller;

import java.util.Optional;
import java.nio.file.Paths;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.ebrozon.model.archivo;
import com.ebrozon.model.usuario;
import com.ebrozon.repository.archivoRepository;
import com.ebrozon.repository.usuarioRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value="Archive Management System", description="Operations pertaining to archive in Archive Managament System ")
public class archivoController {
	
	@Autowired
    archivoRepository repository;
	
	@Autowired
    usuarioRepository repository_u;
	
	private String upload_folder = ".//src//main//resources//files//";
	
	@ApiOperation(value = "Save an archive in the database", response = int.class)
	@CrossOrigin
	@RequestMapping("/uploadFile")
	public int uploadFile(@ApiParam(value = "file to upload", required = true) @RequestParam("file") MultipartFile file) {
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
				return id;//repository.findByurl(upload_folder + fileName).get().getIdentificador();
			}
			catch(IOException e) {
				return -1;
			}
		}
		return -1;
	}
	
	@ApiOperation(value = "Save a temporary archive in the database", response = int.class)
	@CrossOrigin
	@RequestMapping("/uploadArchivoTemp")
	public int uploadArchivoTemp(@ApiParam(value = "file to upload", required = true) @RequestParam("file") String file) {
		if(!file.isEmpty()) {
			try {
				int id = 1;
				Optional<Integer> idAux = repository.lastId();
				if(idAux.isPresent()) {
					id = idAux.get()+1;
				}
				//byte[] array = new byte[10];
				//new Random().nextBytes(array);
				//String fileName = Integer.toString(id)  + "-" + file.getOriginalFilename();
				//saveFile(file, fileName);
				archivo f = new archivo(Integer.toString(id), 0);
				f.setIdentificador(id);
				
				String fileaux = file;
				if(fileaux.substring(0, 4).equals("data")) {
					fileaux = file.substring(11);
					if(fileaux.substring(0, 3).equals("png")) {
						fileaux = fileaux.substring(11);
					}
					else {
						fileaux = fileaux.substring(12);
					}
				}
				
				f.setDatos(fileaux);
				repository.save(f);
				return id;//repository.findByurl(upload_folder + fileName).get().getIdentificador();
			}
			catch(Exception e) {
				return -1;
			}
		}
		return -1;
	}
	
	@ApiOperation(value = "Bring a temporary archive from the database", response = String.class)
	@CrossOrigin
	@RequestMapping("/loadArchivoTemp")
	public String loadArchivoTemp(@ApiParam(value = "id of the tmp archive", required = true) @RequestParam("id") int id) {
		try {
			return repository.findByidentificador(id).get().getDatos();
		}catch(Exception e){return "{O:Ok}";}
	}
	
	@ApiOperation(value = "Bring a user archive from the database", response = String.class)
	@CrossOrigin
	@RequestMapping("/loadArchivoUsuario")
	public String loadArchivoUsuario(@ApiParam(value = "username", required = true) @RequestParam("un") String un) {
		try {
			Optional<usuario> aux = repository_u.findBynombreusuario(un);
			if(!aux.isPresent()) {return "{E:No existe el usuario}";}
			int id = aux.get().getArchivo();
			return repository.findByidentificador(id).get().getDatos();
		}catch(Exception e){return "{O:Ok}";}
	}
	
	@ApiOperation(value = "Bring the url form a file in the database", response = String.class)
	@CrossOrigin
	@RequestMapping("/loadFileUrl")
	public String loadFileUrl(@ApiParam(value = "id of the file", required = true) @RequestParam("id") int id) {
		Optional<archivo> aux = repository.findByidentificador(id);
		if(aux.isPresent()) {
			return aux.get().getUrl();
		}
		return "{E:Ha habido un problema con el archivo.}";
	}
	
	@ApiOperation(value = "Bring a file from the database", response = File.class)
	@CrossOrigin
	@RequestMapping("/loadFile")
	public File loadFile(@ApiParam(value = "id of the file", required = true) @RequestParam("id") int id) {
		Optional<archivo> aux = repository.findByidentificador(id);
		if(aux.isPresent()) {
			try {
				return Paths.get(aux.get().getUrl()).toFile();
			}
			catch(Exception e){return null;}
		}
		return null;
	}
	
	@ApiOperation(value = "Delete a file in the database", response = String.class)
	@CrossOrigin
	@RequestMapping("/deleteFile")
	public String deleteFile(@ApiParam(value = "id of the file", required = true) int id) {
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
