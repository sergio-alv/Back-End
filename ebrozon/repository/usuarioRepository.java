package com.ebrozon.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ebrozon.model.usuario;

public interface usuarioRepository extends CrudRepository<usuario, Long>{
}