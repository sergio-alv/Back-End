package com.ebrozon.model;

import java.util.Date;
import java.io.Serializable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.*;

@Entity
@Table(name = "venta")
//@IdClass(venta.class)
@XmlRootElement
public class venta implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "identificador")
	private int identificador;
	
	
	@Column(name = "usuario")
	@Size(min = 3, max = 30, message="El nombre tiene que tener entre 3 y 30 caracteres")
	@Pattern(regexp = "[A-z,0-9,_,-]+", message="El nombre solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -")
	private String usuario;
	
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
	private double preciofinal;
	
	@Column(name = "comprador")
	private String comprador;
	
	@Column(name = "fechapago")
	private Date fechapago;
	
	@Column(name = "tienearchivo")
	private int tienearchivo;
	
	@Column(name = "activa")
	private int activa;
	
	@Column(name = "es_subasta")
	private int  es_subasta;
	
	@Column(name = "ciudad")
	private String ciudad;
	
	@Transient
	private usuario user;

	public venta(
			@Size(min = 3, max = 30, message = "El nombre tiene que tener entre 3 y 30 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+", message = "El nombre solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -") String usuario,
			@Size(min = 3, max = 100, message = "El nombre del producto tiene que tener entre 3 y 100 caracteres") String producto,
			@Size(min = 10, message = "La descripción del producto tiene que tener mínimo 10 caracteres") String descripcion,
			@Min(0) double precio, int tienearchivo, int activa, String ci) {
		super();
		this.usuario = usuario;
		this.fechainicio = new Date();
		this.producto = producto;
		this.descripcion = descripcion;
		this.precio = precio;
		this.tienearchivo = tienearchivo;
		this.activa = activa;
		this.es_subasta = 0;
		this.ciudad = ci;
	}
	
	public venta() {}
	
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

	public void setFechaventa() {
		this.fechaventa = new Date();
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

	public int getTienearchivo() {
		return tienearchivo;
	}

	public void setTienearchivo(int tienearchivo) {
		this.tienearchivo = tienearchivo;
	}

	public int getActiva() {
		return activa;
	}

	public void setActiva(int activa) {
		this.activa = activa;
	}

	public int getes_subasta() {
		return es_subasta;
	}

	public void setes_subasta(int es_subasta) {
		this.es_subasta = es_subasta;
	}
	
	public usuario getUser() {
		return this.user;
	}

	public void setUser(usuario us) {
		this.user = us;
	}

	public int getIdentificador() {
		return identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	
}
