package com.ebrozon.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mensaje")
public class mensaje {
	
	 public mensaje(int identificador, String emisor, String receptor, String contenido) {
		super();
		this.identificador = identificador;
		this.emisor = emisor;
		this.receptor = receptor;
		this.contenido = contenido;
		this.fecha = new Date();
	}
	 
	 public mensaje(String emisor, String receptor, String contenido) {
			super();
			this.emisor = emisor;
			this.receptor = receptor;
			this.contenido = contenido;
			this.fecha = new Date();
		}

	@Id
	 @Column(name = "identificador")
	 private int identificador;
	 
	 @Column(name = "emisor")
	 private String emisor;
	 
	 @Column(name = "receptor")
	 private String receptor;
	 
	 @Column(name = "fecha")
	 protected Date fecha;
	 
	 @Column(name = "contenido")
	 private String contenido;
	 
	 public mensaje() {}

	public int getIdentificador() {
		return identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}

	public String getEmisor() {
		return emisor;
	}

	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}

	public String getReceptor() {
		return receptor;
	}

	public void setReceptor(String receptor) {
		this.receptor = receptor;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	};
}
