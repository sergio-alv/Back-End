package com.ebrozon.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "opinion")
public class opinion {
	public opinion(int identificador, String emisor, String receptor, String contenido, double estrellas) {
		super();
		this.identificador = identificador;
		this.emisor = emisor;
		this.receptor = receptor;
		this.fecha = new Date();
		this.contenido = contenido;
		this.estrellas = estrellas;
		this.tienearchivo = 0;
	}
	public opinion() {}
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
	 
	 @Column(name="estrellas")
	 private double estrellas;
	 
	 @Column(name="tienearchivo")
	 private int tienearchivo;

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
	}

	public double getEstrellas() {
		return estrellas;
	}

	public void setEstrellas(double estrellas) {
		this.estrellas = estrellas;
	}
	 
	 
}
