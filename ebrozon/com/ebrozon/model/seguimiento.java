package com.ebrozon.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "seguimiento")
@ApiModel(description = "All details about a track. ")
public class seguimiento {
	
	@Id
	@ApiModelProperty(notes = "Track's identifier")
	@Column(name = "identificador")
	private int identificador;
	
	@ApiModelProperty(notes = "User of the track")
	@Column(name = "usuario")
	@Size(min = 3, max = 30, message="El nombre del usuario tiene que tener entre 3 y 30 caracteres")
	@Pattern(regexp = "[A-z,0-9,_,-]+", message="El nombre del usuario solo puede tener letras mayusculas o minusculas sin acentuar, numeros, y los caracteres _ y -")
	private String usuario;
	
	@ApiModelProperty(notes = "Sale of the track")
	@Column(name = "nventa")
	private int nventa;
	
	@ApiModelProperty(notes = "Track's date")
	@Column(name = "fecha")
	@NotNull
	private Date fecha;
	
	@ApiModelProperty(notes = "Track's product")
	@Column(name = "producto")
	private String producto;
	
	public seguimiento() {}
	
	public seguimiento(
			@Size(min = 3, max = 30, message="El usuario tiene que tener entre 3 y 30 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+", message="El usuario solo puede tener letras mayusculas o minusculas sin acentuar, numeros, y los caracteres _ y -") String usuario,
			@Size(max = 11) int nventa, 
      			String producto) {
		super();
		this.usuario = usuario;
		this.nventa = nventa;
		this.fecha = new Date();
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
  
  	public String getProducto() {
		return producto;
	}
	
	public void setProducto(String producto) {
		this.producto = producto;
	}

	public int getIdentificador() {
		return identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}
}
