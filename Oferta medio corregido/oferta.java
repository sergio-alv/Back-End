package com.ebrozon.model;

import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "oferta")
@IdClass(oferta.class)
public class oferta implements Serializable{
	@Id
	@Column(name = "usuario")
	@Size(min = 3, max = 30, message="El nombre del usuario tiene que tener entre 3 y 30 caracteres")
	@Pattern(regexp = "[A-z,0-9,_,-]+", message="El nombre del usuario solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -")
	private String usuario;
	
	@Id
	@Column(name = "nventa")
	private int nventa;
	
	@Id
	@Column(name = "fecha")
	@NotNull
	private Date fecha;
	
	@Column(name = "cantidad")
	@NotNull
	private float cantidad;
	
	@Column(name = "aceptada")
	@NotNull
	private short aceptada;
	
	@Transient
	private String producto;
	
	
	public oferta(
			@Size(min = 3, max = 30, message="El usuario tiene que tener entre 3 y 30 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+", message="El usuario solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -") String usuario,
			int nventa, @NotNull Date fecha, @NotNull float cantidad, @NotNull short aceptada, String producto) {
		super();
		this.usuario = usuario;
		this.nventa = nventa;
		this.fecha = fecha;
		this.cantidad = cantidad;
		this.aceptada = aceptada;
		this.producto = producto;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public int getNventa() {
		return nventa;
	}

	public void setNventa(int nventa) {
		this.nventa = nventa;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public float getCantidad() {
		return cantidad;
	}

	public void setCantidad(float cantidad) {
		this.cantidad = cantidad;
	}

	public short getAceptada() {
		return aceptada;
	}

	public void setAceptada(short aceptada) {
		this.aceptada = aceptada;
	}
	
	public String getProducto() {
		return producto;
	}
	
	public void setProducto(String producto) {
		this.producto = producto;
	}
}