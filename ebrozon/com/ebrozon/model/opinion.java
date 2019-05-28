package com.ebrozon.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "opinion")
@ApiModel(description = "All details about an opinion. ")
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
	 @ApiModelProperty(notes = "Opinion's identifier")
	 @Column(name = "identificador")
	 private int identificador;
	 
	 @ApiModelProperty(notes = "Opinion's transmitter")
	 @Column(name = "emisor")
	 private String emisor;
	 
	 @ApiModelProperty(notes = "Opinion's reciever")
	 @Column(name = "receptor")
	 private String receptor;
	 
	 @ApiModelProperty(notes = "Opinion's date")
	 @Column(name = "fecha")
	 protected Date fecha;
	 
	 @ApiModelProperty(notes = "Opinion's content")
	 @Column(name = "contenido")
	 private String contenido;
	 
	 @ApiModelProperty(notes = "Opinion's valuation")
	 @Column(name="estrellas")
	 private double estrellas;
	 
	 @ApiModelProperty(notes = "If a opinion has an archive")
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
