package com.ebrozon.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "etiqueta")
public class etiqueta {
	
	@Id
	@Column(name = "nombre")
	@Size(min = 3, max = 30, message="El nombre de la etiqueta tiene que tener entre 3 y 30 caracteres")
	@Pattern(regexp = "[A-z,0-9,_,-]+", message="El nombre de la etiqueta solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -")
	private String nombre;
	
	@Column(name = "fechacreacion")
	@NotNull
	private Date fechacreacion;
	
	@Column(name = "nombreusuario")
	@Size(min = 3, max = 30, message="El nombre del creador tiene que tener entre 3 y 30 caracteres")
	@Pattern(regexp = "[A-z,0-9,_,-]+", message="El nombre del creador solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -")
	private String creador;
	
	public etiqueta(
			@Size(min = 3, max = 30, message="El nombre de la etiqueta tiene que tener entre 3 y 30 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+", message="El nombre de la etiqueta solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -") String nombre,
			@NotNull Date fechacreacion,
			@Size(min = 3, max = 30, message="El nombre del creador tiene que tener entre 3 y 30 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+", message="El nombre del creador solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -") String creador) {
		super();
		this.nombre = nombre;
		this.fechacreacion = fechacreacion;
		this.creador = creador;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFechacreacion() {
		return fechacreacion;
	}

	public void setFechacreacion(Date fechacreacion) {
		this.fechacreacion = fechacreacion;
	}

	public String getCreador() {
		return creador;
	}

	public void setCreador(String creador) {
		this.creador = creador;
	}
	
	
}