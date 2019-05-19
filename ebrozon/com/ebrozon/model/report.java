package com.ebrozon.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "report")
public class report {

	public report(String emisor, String receptor, String contenido, int nventa, String motivo) {
		super();
		this.emisor = emisor;
		this.receptor = receptor;
		this.contenido = contenido;
		this.nventa = nventa;
		this.motivo = motivo;
		this.tienearchivo = 0;
		this.fecha = new Date();
	}
	
	public report(String emisor, String receptor, String contenido, String motivo) {
		super();
		this.emisor = emisor;
		this.receptor = receptor;
		this.contenido = contenido;
		this.motivo = motivo;
		this.tienearchivo = 0;
		this.fecha = new Date();
	}

	public report() {}
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
	 
	 @Column(name="nventa")
	 private int nventa;
	 
	 @Column(name = "motivo")
	 private String motivo;
	 
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

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public int getNventa() {
		return nventa;
	}

	public void setNventa(int nventa) {
		this.nventa = nventa;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

}
