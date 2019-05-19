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

@Entity
@Table(name = "usuarioverant")
@IdClass(usuarioverant.class)
public class usuarioverant implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	 @Id
	 @Column(name = "nombreusuario")
	 @Size(min = 3, max = 30, message="El nombre tiene que tener entre 3 y 30 caracteres")
	 @Pattern(regexp = "[A-z,0-9,_,-]+", message="El nombre solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -")
	 private String nombreusuario;
	 
	 @Id
	 private Date fecha;
	 
	 @Column(name = "telefono")
	 //@Range(min=100000000,max=999999999, message = "El teléfono tiene que tener 9 dígitos.")
	 private int telefono;
	 
	 @Column(name = "nombre")
	 @NotNull
	 private String nombre;
	 
	 @Column(name = "apellidos")
	 @NotNull
	 private String apellidos;
	 
	 @Column(name = "codigopostal")
	//@Range(min=1,max=99999, message = "El código postal tiene que tener un mínimo de 1 dígito y un máximo de 5.")
	 private int codigopostal;
	 
	 @Column(name = "ciudad")
	 private String ciudad;
	 
	 @Column(name = "provincia")
	 private String provincia;
	 
	 @Column(name = "latitud")
	 private double latitud;
	 
	 @Column(name = "longitud")
	 private double longitud;
	 
	 @Column(name = "archivo")
	 @Min(0)
	 private int archivo;
	 
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