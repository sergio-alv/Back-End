package com.ebrozon.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "archivo")
public class archivo {
	public archivo() {}
	 public archivo(String url, int borrada) {
		super();
		this.url = url;
		this.borrada = borrada;
	}

	 @Id
	 @Column(name = "identificador")
	 private int identificador;
	 
	 @Column(name = "url")
	 private String url;
	 
	 @Column(name = "borrada")
	 private int borrada;
	 
	 @Column(name="datos")
	 private String datos;

	public int getIdentificador() {
		return identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getBorrada() {
		return borrada;
	}

	public void setBorrada(int borrada) {
		this.borrada = borrada;
	}
	public String getDatos() {
		return datos;
	}
	public void setDatos(String datos) {
		this.datos = datos;
	}
}
