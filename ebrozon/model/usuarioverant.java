package com.ebrozon.model;

import java.io.Serializable;
import java.sql.Date;

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
@Table(name = "usuarioverant")
public class usuarioverant {
	
	 @Id
	 @Column(name = "nombreusuario")
	 @Size(min = 3, max = 30, message="El nombre tiene que tener entre 3 y 30 caracteres")
	 @Pattern(regexp = "[A-z,0-9,_,-]+", message="El nombre solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -")
	 private String nombreusuario;
	 
	 @Id
	 private Date fecha;
	 
	 @Column(name = "telefono")
	 @Min(100000000)
	 @Max(999999999)
	 private int telefono;
	 
	 @Column(name = "nombre")
	 @NotNull
	 private String nombre;
	 
	 @Column(name = "apellidos")
	 @NotNull
	 private String apellidos;
	 
	 @Column(name = "codigopostal")
	 @Min(00001)
	 @Max(99999)
	 private int codigopostal;
	 
	 @Column(name = "ciudad")
	 @NotNull
	 private String ciudad;
	 
	 @Column(name = "provincia")
	 @NotNull
	 private String provincia;
	 
	 @Column(name = "latitud")
	 private float latitud;
	 
	 @Column(name = "longitud")
	 private float longitud;
	 
	 @Column(name = "imagen")
	 private int imagen;
	 
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
		this.imagen = u.getArchivo();
		this.activo = u.getActivo();
	}
	
	public usuarioverant() {}
	
}