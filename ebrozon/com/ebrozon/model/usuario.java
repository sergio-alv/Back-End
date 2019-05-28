package com.ebrozon.model;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@Table(name = "usuario")
@XmlRootElement
@ApiModel(description = "All details about a user. ")
public class usuario implements Serializable{
	
	protected static final long serialVersionUID = 1L;
	
	 @Id
	 @ApiModelProperty(notes = "User's username")
	 @Column(name = "nombreusuario")
	 @Size(min = 3, max = 30, message="El nombre tiene que tener entre 3 y 30 caracteres.")
	 @Pattern(regexp = "[A-z,0-9,_,-]+", message="El nombre solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -.")
	 private String nombreusuario;
	 
	 @ApiModelProperty(notes = "User's mail")
	 @Column(name = "correo")
	 @Size(min = 3, max = 100, message="El correo tiene que tener entre 3 y 100 caracteres.")
	 @Pattern(regexp = "[^@]+@[A-z,0-9]+.[A-z]+", message="El correo tiene que seguir el patron example@example.example.")
	 private String correo;
	 
	 @ApiModelProperty(notes = "User's password")
	 @Column(name = "contrasena")
	 @Size(min = 8, max = 100, message="La contraseña tiene que tener entre 8 y 100 caracteres.")
	 @Pattern(regexp = "[A-z,0-9,_,-]+", message="La contraseña solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -.")
	 private String contrasena;
	 
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
	 
	 @ApiModelProperty(notes = "User's valuation")
	 @Column(name = "estrellas")
	 private double estrellas;
	 
	 @Transient
	 private String urlArchivo;

	 public usuario(
				@Size(min = 3, max = 30, message = "El nombre tiene que tener entre 3 y 30 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+", message = "El nombre solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -.") String nombreusuario,
				@Size(min = 3, max = 10, message = "El correo tiene que tener entre 3 y 100 caracteres") @Pattern(regexp = "[^@]+@[A-z,0-9]+.[A-z]+", message="El correo tiene que seguir el patron example@example.example.") String correo,
				@Size(min = 8, max = 100, message = "El correo tiene que tener entre 8 y 100 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+", message = "La contraseña solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -.") String contrasena,
				@NotNull String nombre, @NotNull String apellidos) {
			super();
			this.nombreusuario = nombreusuario;
			this.correo = correo;
			this.contrasena = contrasena;
			this.nombre = nombre;
			this.apellidos = apellidos;
			
			this.codigopostal = 0;
			this.ciudad = "";
			this.provincia = "";
			this.latitud = 0.0;
			this.longitud = 0.0;
			this.archivo = 0;
			this.telefono = 0;
			
			this.estrellas = 0.0;
		}
	 
	 public usuario(
				@Size(min = 3, max = 30, message = "El nombre tiene que tener entre 3 y 30 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+", message = "El nombre solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -.") String nombreusuario,
				@Size(min = 3, max = 10, message = "El correo tiene que tener entre 3 y 100 caracteres") @Pattern(regexp = "[^@]+@[A-z,0-9]+.[A-z]+", message = "El correo solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -.") String correo,
				@Size(min = 8, max = 100, message = "El correo tiene que tener entre 8 y 100 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+", message = "La contraseña solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -.") String contrasena,
				@Min(100000000) @Max(999999999) int telefono, @NotNull String nombre, @NotNull String apellidos,
				@Min(1) @Max(99999) int codigopostal, @NotNull String ciudad, @NotNull String provincia) {
			super();
			this.nombreusuario = nombreusuario;
			this.correo = correo;
			this.contrasena = contrasena;
			this.telefono = telefono;
			this.nombre = nombre;
			this.apellidos = apellidos;
			this.codigopostal = codigopostal;
			this.ciudad = ciudad;
			this.provincia = provincia;
			this.estrellas = 0.0;
			this.longitud=(float)-9999;
			this.latitud=(float)-9999;
		}

		public usuario(
				@Size(min = 3, max = 30, message = "El nombre tiene que tener entre 3 y 30 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+", message = "El nombre solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -.") String nombreusuario,
				@Size(min = 3, max = 10, message = "El correo tiene que tener entre 3 y 100 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+@[A-z]+.[A-z]+", message = "El correo solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -.") String correo,
				@Size(min = 8, max = 100, message = "El correo tiene que tener entre 8 y 100 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+", message = "La contraseña solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -.") String contrasena,
				@Min(100000000) @Max(999999999) int telefono, @NotNull String nombre, @NotNull String apellidos,
				@Min(1) @Max(99999) int codigopostal, @NotNull String ciudad,@NotNull String provincia,
				double latitud, double longitud, @Min(0) int archivo) {
			super();
			this.nombreusuario = nombreusuario;
			this.correo = correo;
			this.contrasena = contrasena;
			this.telefono = telefono;
			this.nombre = nombre;
			this.apellidos = apellidos;
			this.codigopostal = codigopostal;
			this.ciudad = ciudad;
			this.provincia = provincia;
			this.latitud = latitud;
			this.longitud = longitud;
			this.archivo = archivo;
			this.estrellas = 0.0;
			this.longitud=(float)-9999;
			this.latitud=(float)-9999;
		}
	public usuario() {
		this.longitud=(float)-9999;
		this.latitud=(float)-9999;
	}
	 
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

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(@Min(100000000) @Max(999999999) int telefono) {
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

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public int getArchivo() {
		return archivo;
	}

	public void setArchivo(int archivo) {
		this.archivo = archivo;
	}

	public int getActivo() {
		return activo;
	}

	public void setActivo(int activo) {
		this.activo = activo;
	}
	
	public void setUrlArchivo(String url) {
		this.urlArchivo = url;
	}
	
	public String getUrlArchivo() {
		return this.urlArchivo;
	}

	public double getEstrellas() {
		return estrellas;
	}

	public void setEstrellas(double estrellas) {
		this.estrellas = estrellas;
	}
	 
	/*
	 @Override
	 public String toString() {
	 return String.format("Customer[id=%d, firstName='%s', lastName='%s']", id, firstName, lastName);
	 }*/
}
