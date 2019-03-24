package com.ebrozon.model;

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
@Table(name = "venta")
public class venta {

	public venta(
			@Size(min = 3, max = 30, message = "El nombre tiene que tener entre 3 y 30 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+", message = "El nombre solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -") String usuario,
			Date fechaventa,
			@Size(min = 3, max = 100, message = "El nombre del producto tiene que tener entre 3 y 100 caracteres") String producto,
			@Size(min = 10, message = "La descripción del producto tiene que tener mínimo 10 caracteres") String descripcion,
			@Min(0) double precio, int tienevideo, int activa, int esSubasta) {
		super();
		this.usuario = usuario;
		this.fechaventa = fechaventa;
		this.producto = producto;
		this.descripcion = descripcion;
		this.precio = precio;
		this.tienevideo = tienevideo;
		this.activa = activa;
		this.esSubasta = esSubasta;
	}

	@Id
	@Column(name = "usuario")
	@Size(min = 3, max = 30, message="El nombre tiene que tener entre 3 y 30 caracteres")
	@Pattern(regexp = "[A-z,0-9,_,-]+", message="El nombre solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -")
	private String usuario;
	
	@Id
	@Column(name = "fechainicio")
	private Date fechainicio;
	
	@Column(name = "fechaventa")
	private Date fechaventa;
	
	@Column(name = "producto")
	@Size(min = 3, max = 100, message="El nombre del producto tiene que tener entre 3 y 100 caracteres")
	private String producto;
	
	@Column(name = "descripcion")
	@Size(min = 10, message="La descripción del producto tiene que tener mínimo 10 caracteres")
	private String descripcion;
	
	@Column(name = "precio")
	@Min(0)
	private double precio;
	
	@Column(name = "preciofinal")
	@Min(0)
	private double preciofinal;
	
	@Column(name = "comprador")
	private String comprador;
	
	@Column(name = "fechapago")
	private Date fechapago;
	
	@Column(name = "tienevideo")
	private int tienevideo;
	
	@Column(name = "activa")
	private int activa;
	
	@Column(name = "esSubasta")
	private int  esSubasta;

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getFechainicio() {
		return fechainicio;
	}

	public void setFechainicio(Date fechainicio) {
		this.fechainicio = fechainicio;
	}

	public Date getFechaventa() {
		return fechaventa;
	}

	public void setFechaventa(Date fechaventa) {
		this.fechaventa = fechaventa;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public double getPreciofinal() {
		return preciofinal;
	}

	public void setPreciofinal(double preciofinal) {
		this.preciofinal = preciofinal;
	}

	public String getComprador() {
		return comprador;
	}

	public void setComprador(String comprador) {
		this.comprador = comprador;
	}

	public Date getFechapago() {
		return fechapago;
	}

	public void setFechapago(Date fechapago) {
		this.fechapago = fechapago;
	}

	public int getTienevideo() {
		return tienevideo;
	}

	public void setTienevideo(int tienevideo) {
		this.tienevideo = tienevideo;
	}

	public int getActiva() {
		return activa;
	}

	public void setActiva(int activa) {
		this.activa = activa;
	}

	public int getEsSubasta() {
		return esSubasta;
	}

	public void setEsSubasta(int esSubasta) {
		this.esSubasta = esSubasta;
	}
	
}
