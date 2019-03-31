package com.ebrozon.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "usuarioverant")
@IdClass(usuarioverant.class)
public class usuarioverant implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
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
		this.nombre = u.getNombre();
		this.apellidos = u.getApellidos();
		this.activo = u.getActivo();
	}
	
	public usuarioverant() {}

	public String getNombreusuario() {
		return nombreusuario;
	}

	public void setNombreusuario(String nombreusuario) {
		this.nombreusuario = nombreusuario;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(@Min(100000000) @Max(999999999)int telefono) {
		this.telefono = telefono;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public int getCodigopostal() {
		return codigopostal;
	}

	public void setCodigopostal(@Min(0) @Max(99999)int codigopostal) {
		this.codigopostal = codigopostal;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public float getLatitud() {
		return latitud;
	}

	public void setLatitud(float latitud) {
		this.latitud = latitud;
	}

	public float getLongitud() {
		return longitud;
	}

	public void setLongitud(float longitud) {
		this.longitud = longitud;
	}

	public int getImagen() {
		return imagen;
	}

	public void setImagen(int imagen) {
		this.imagen = imagen;
	}

	public int getActivo() {
		return activo;
	}

	public void setActivo(int activo) {
		this.activo = activo;
	}
	
}