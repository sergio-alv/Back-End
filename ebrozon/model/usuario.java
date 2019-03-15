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
@Table(name = "usuario")
public class usuario {
	
	 @Id
	 @Column(name = "nombreusuario")
	 @Size(min = 3, max = 30, message="El nombre tiene que tener entre 3 y 30 caracteres")
	 @Pattern(regexp = "[A-z,0-9,_,-]+", message="El nombre solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -")
	 private String nombreusuario;
	 
	 @Column(name = "correo")
	 @Size(min = 3, max = 100, message="El correo tiene que tener entre 3 y 100 caracteres")
	 @Pattern(regexp = "[A-z,0-9,_,-]+@[A-z]+.[A-z]+", message="El correo solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -")
	 private String correo;
	 
	 @Column(name = "contrasena")
	 @Size(min = 8, max = 100, message="La contraseña tiene que tener entre 8 y 100 caracteres")
	 @Pattern(regexp = "[A-z,0-9,_,-]+", message="La contraseña solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -")
	 private String contrena;
	 
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
	 
	 @Column(name = "latitud")
	 private float latitud;
	 
	 @Column(name = "longitud")
	 private float longitud;
	 
	 @Column(name = "archivo")
	 @Min(0)
	 private int archivo;
	 
	 @Column(name = "activo")
	 private boolean activo;

	 public usuario(
				@Size(min = 3, max = 30, message = "El nombre tiene que tener entre 3 y 30 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+", message = "El nombre solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -") String nombreusuario,
				@Size(min = 3, max = 10, message = "El correo tiene que tener entre 3 y 100 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+@[A-z]+.[A-z]+", message = "El correo solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -") String correo,
				@Size(min = 8, max = 100, message = "El correo tiene que tener entre 8 y 100 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+", message = "La contraseña solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -") String contrena,
				@Min(100000000) @Max(999999999) int telefono, @NotNull String nombre, @NotNull String apellidos,
				@Min(1) @Max(99999) int codigopostal, @NotNull String ciudad) {
			super();
			this.nombreusuario = nombreusuario;
			this.correo = correo;
			this.contrena = contrena;
			this.telefono = telefono;
			this.nombre = nombre;
			this.apellidos = apellidos;
			this.codigopostal = codigopostal;
			this.ciudad = ciudad;
		}

		public usuario(
				@Size(min = 3, max = 30, message = "El nombre tiene que tener entre 3 y 30 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+", message = "El nombre solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -") String nombreusuario,
				@Size(min = 3, max = 10, message = "El correo tiene que tener entre 3 y 100 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+@[A-z]+.[A-z]+", message = "El correo solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -") String correo,
				@Size(min = 8, max = 100, message = "El correo tiene que tener entre 8 y 100 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+", message = "La contraseña solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -") String contrena,
				@Min(100000000) @Max(999999999) int telefono, @NotNull String nombre, @NotNull String apellidos,
				@Min(1) @Max(99999) int codigopostal, @NotNull String ciudad, float latitud, float longitud,
				@Min(0) int archivo) {
			super();
			this.nombreusuario = nombreusuario;
			this.correo = correo;
			this.contrena = contrena;
			this.telefono = telefono;
			this.nombre = nombre;
			this.apellidos = apellidos;
			this.codigopostal = codigopostal;
			this.ciudad = ciudad;
			this.latitud = latitud;
			this.longitud = longitud;
			this.archivo = archivo;
		}
	public usuario() {}
	 
	public String getNombreusuario() {
		return nombreusuario;
	}

	public void setNombreusuario(String nombreusuario) {
		this.nombreusuario = nombreusuario;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getContrena() {
		return contrena;
	}

	public void setContrena(String contrena) {
		this.contrena = contrena;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
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

	public void setCodigopostal(int codigopostal) {
		this.codigopostal = codigopostal;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
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

	public int getArchivo() {
		return archivo;
	}

	public void setArchivo(int archivo) {
		this.archivo = archivo;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	 
	/*
	 @Override
	 public String toString() {
	 return String.format("Customer[id=%d, firstName='%s', lastName='%s']", id, firstName, lastName);
	 }*/
}
