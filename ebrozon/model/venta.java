package com.ebrozon.model;

import java.util.Date;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.*;

@Entity
@Table(name = "venta")
//@IdClass(venta.class)
@Inheritance(strategy = InheritanceType.JOINED)
@XmlRootElement
public class venta implements Serializable{

	protected static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "identificador")
	protected int identificador;
	
	@Column(name = "usuario")
	@Size(min = 3, max = 30, message="El nombre tiene que tener entre 3 y 30 caracteres")
	@Pattern(regexp = "[A-z,0-9,_,-]+", message="El nombre solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -")
	protected String usuario;
	
	@Column(name = "fechainicio")
	protected Date fechainicio;
	
	@Column(name = "fechaventa")
	protected Date fechaventa;
	
	@Column(name = "producto")
	@Size(min = 3, max = 100, message="El nombre del producto tiene que tener entre 3 y 100 caracteres")
	protected String producto;
	
	@Column(name = "descripcion")
	@Size(min = 10, message="La descripción del producto tiene que tener mínimo 10 caracteres")
	protected String descripcion;
	
	@Column(name = "precio")
	@Min(0)
	protected double precio;
	
	@Column(name = "preciofinal")
	protected double preciofinal;
	
	@Column(name = "comprador")
	protected String comprador;
	
	@Column(name = "fechapago")
	protected Date fechapago;
	
	@Column(name = "tienearchivo")
	protected int tienearchivo;
	
	@Column(name = "activa")
	protected int activa;
	
	@Column(name = "es_subasta")
	protected int  es_subasta;
	
	@Column(name = "ciudad")
	protected String ciudad;
	
	@Column(name = "provincia")
	protected String provincia;
	
	@Transient
	protected usuario user;
	
	@Transient
	protected List<Integer> archivos;

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

	public void setFechaventa(Date date) {
		this.fechaventa = date;
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

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public List<Integer> getArchivos() {
		return archivos;
	}

	public void setArchivos(List<Integer> archivos) {
		this.archivos = archivos;
	}
	
}
