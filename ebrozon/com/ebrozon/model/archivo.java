package com.ebrozon.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "archivo")
@ApiModel(description = "All details about an archive. ")
public class archivo {
	public archivo() {}
	 public archivo(String url, int borrada) {
		super();
		this.url = url;
		this.borrada = borrada;
	}

	 @Id
	 @ApiModelProperty(notes = "Archive's identifier")
	 @Column(name = "identificador")
	 private int identificador;
	 
	 @ApiModelProperty(notes = "Archive's url")
	 @Column(name = "url")
	 private String url;
	 
	 @ApiModelProperty(notes = "Archive deleted from the database")
	 @Column(name = "borrada")
	 private int borrada;
	 
	 @ApiModelProperty(notes = "Data from the archive")
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
