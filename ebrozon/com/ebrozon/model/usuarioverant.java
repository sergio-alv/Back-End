package com.ebrozon.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "usuarioverant")
@IdClass(usuarioverant.class)
@ApiModel(description = "All details about historical user information. ")
public class usuarioverant implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	 @Id
	 @ApiModelProperty(notes = "User's username")
	 @Column(name = "nombreusuario")
	 @Size(min = 3, max = 30, message="El nombre tiene que tener entre 3 y 30 caracteres")
	 @Pattern(regexp = "[A-z,0-9,_,-]+", message="El nombre solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -")
	 private String nombreusuario;
	 
	 @Id
	 @ApiModelProperty(notes = "Date of the user's information change")
	 private Date fecha;
	 
	 @ApiModelProperty(notes = "User's telephone number")
	 @Column(name = "telefono")
	 //@Range(min=100000000,max=999999999, message = "El teléfono tiene que tener 9 dígitos.")
	 private int telefono;
	 
	 @ApiModelProperty(notes = "User's name")
	 @Column(name = "nombre")
	 @NotNull
	 private String nombre;
	 
	 @ApiModelProperty(notes = "User's family name")
	 @Column(name = "apellidos")
	 @NotNull
	 private String apellidos;
	 
	 @ApiModelProperty(notes = "User's postal code") 
	 @Column(name = "codigopostal")
	//@Range(min=1,max=99999, message = "El código postal tiene que tener un mínimo de 1 dígito y un máximo de 5.")
	 private int codigopostal;
	 
	 @ApiModelProperty(notes = "User's city")
	 @Column(name = "ciudad")
	 private String ciudad;
	 
	 @ApiModelProperty(notes = "User's province")
	 @Column(name = "provincia")
	 private String provincia;
	 
	 @ApiModelProperty(notes = "User's ubication latitude")
	 @Column(name = "latitud")
	 private double latitud;
	 
	 @ApiModelProperty(notes = "User's ubication longitude")
	 @Column(name = "longitud")
	 private double longitud;
	 
	 @ApiModelProperty(notes = "User's archive")
	 @Column(name = "archivo")
	 @Min(0)
	 private int archivo;
	 
	 @ApiModelProperty(notes = "If a user is allowed or banned")
	 @Column(name = "activo")
	 private int activo;

	public usuarioverant(usuario u) {
		super();
		this.nombreusuario = u.getNombreusuario();
		this.telefono = u.getTelefono();
		this.nombre = u.getNombre();
		this.apellidos = u.getApellidos();
		this.codigopostal = u.getCodigopostal();
		this.ciudad = u.getCiudad();
		this.provincia = u.getProvincia();
		this.latitud = u.getLatitud();
		this.longitud = u.getLongitud();
		this.archivo = u.getArchivo();
		this.activo = u.getActivo();
		this.fecha = new Date();
	}
	
	public usuarioverant() {}
}