package com.ebrozon.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ventaverant")
@IdClass(ventaverant.class)
public class ventaverant implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "nventa")
	private int nventa;
	
	@Id
	private Date fechamod;
	
	@Column(name = "producto")
	@Size(min = 3, max = 100, message="El nombre del producto tiene que tener entre 3 y 100 caracteres")
	private String producto;
	
	@Column(name = "descripcion")
	@Size(min = 10, message="La descripción del producto tiene que tener mínimo 10 caracteres")
	private String descripcion;
	
	@Column(name = "precio")
	@Min(0)
	private double precio;
	
	@Column(name = "tienearchivo")
	private int tienearchivo;
	
	@Column(name = "activa")
	private int activa;
	
	public ventaverant(venta v) {
		super();
		this.nventa = v.getIdentificador();
		this.producto = v.getProducto();
		this.descripcion = v.getDescripcion();
		this.precio = v.getPrecio();
		this.tienearchivo = v.getTienearchivo();
		this.activa = v.getActiva();
	}
}
