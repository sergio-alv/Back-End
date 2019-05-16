package com.ebrozon.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "etiqueta")
public class etiqueta {
	
	@Id
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "fechacreacion")
	@NotNull
	private Date fechacreacion;
	
	@Column(name = "creador")
	@Size(min = 3, max = 30, message="El nombre del creador tiene que tener entre 3 y 30 caracteres")
	@Pattern(regexp = "[A-z,0-9,_,-]+", message="El nombre del creador solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -")
	private String creador;
	
	public etiqueta() {}
	
	public etiqueta(String nombre,
			@Size(min = 3, max = 30, message="El nombre del creador tiene que tener entre 3 y 30 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+", message="El nombre del creador solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -") String creador) {
		super();
		this.nombre = nombre;
		this.fechacreacion = new Date();
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