package com.ebrozon.model;

import java.io.Serializable;

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
@Table(name = "archivo")
public class archivo {
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
}
