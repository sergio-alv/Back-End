package com.ebrozon.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "ventaverant")
@IdClass(ventaverant.class)
@ApiModel(description = "All details about historical sales information. ")
public class ventaverant implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@ApiModelProperty(notes = "Sale's identifier")
	@Column(name = "nventa")
	private int nventa;
	
	@Id
	@ApiModelProperty(notes = "Date of modification of a sale")
	private Date fechamod;
	
	@ApiModelProperty(notes = "Sale's product")
	@Column(name = "producto")
	@Size(min = 3, max = 100, message="El nombre del producto tiene que tener entre 3 y 100 caracteres")
	private String producto;
	
	@ApiModelProperty(notes = "Sale's description")
	@Column(name = "descripcion")
	@Size(min = 10, message="La descripción del producto tiene que tener mínimo 10 caracteres")
	private String descripcion;
	
	@ApiModelProperty(notes = "Sale's price")
	@Column(name = "precio")
	@Min(0)
	private double precio;
	
	@ApiModelProperty(notes = "Sale's archive")
	@Column(name = "tienearchivo")
	private int tienearchivo;
	
	@ApiModelProperty(notes = "If a sale is active or not")
	@Column(name = "activa")
	private int activa;
	
	@ApiModelProperty(notes = "Sale's category")
	@Column(name = "categoria")
	private String categoria;
	
	public ventaverant(venta v) {
		super();
		this.nventa = v.getIdentificador();
		this.producto = v.getProducto();
		this.descripcion = v.getDescripcion();
		this.precio = v.getPrecio();
		this.tienearchivo = v.getTienearchivo();
		this.activa = v.getActiva();
		this.fechamod = new Date();
		this.categoria = v.getCategoria();
	}
	
	public ventaverant() {}
}