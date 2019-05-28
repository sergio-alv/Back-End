package com.ebrozon.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "mensaje")
@ApiModel(description = "All details about a message. ")
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
	 @ApiModelProperty(notes = "Message's identifier")
	 @Column(name = "identificador")
	 private int identificador;
	 
	 @ApiModelProperty(notes = "Message's transmitter")
	 @Column(name = "emisor")
	 private String emisor;
	 
	 @ApiModelProperty(notes = "Message's reciever")
	 @Column(name = "receptor")
	 private String receptor;
	 
	 @ApiModelProperty(notes = "Message's date")
	 @Column(name = "fecha")
	 protected Date fecha;
	 
	 @ApiModelProperty(notes = "Message's content")
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
