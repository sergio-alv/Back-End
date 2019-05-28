package com.ebrozon.model;

import java.util.Date;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@Table(name = "venta")
//@IdClass(venta.class)
@Inheritance(strategy = InheritanceType.JOINED)
@XmlRootElement
@ApiModel(description = "All details about a sale. ")
public class venta implements Serializable, Comparable< venta >{

	protected static final long serialVersionUID = 1L;
	
	@Id
	@ApiModelProperty(notes = "Sale's identifier")
	@Column(name = "identificador")
	protected int identificador;
	
	@ApiModelProperty(notes = "Sale's creator")
	@Column(name = "usuario")
	@Size(min = 3, max = 30, message="El nombre tiene que tener entre 3 y 30 caracteres")
	@Pattern(regexp = "[A-z,0-9,_,-]+", message="El nombre solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -")
	protected String usuario;
	
	@ApiModelProperty(notes = "Sale's initial date")
	@Column(name = "fechainicio")
	protected Date fechainicio;
	
	@ApiModelProperty(notes = "Sale's sale date")
	@Column(name = "fechaventa")
	protected Date fechaventa;
	
	@ApiModelProperty(notes = "Sale's product")
	@Column(name = "producto")
	@Size(min = 3, max = 100, message="El nombre del producto tiene que tener entre 3 y 100 caracteres")
	protected String producto;
	
	@ApiModelProperty(notes = "Sale's description")
	@Column(name = "descripcion")
	@Size(min = 10, message="La descripción del producto tiene que tener mínimo 10 caracteres")
	protected String descripcion;
	
	@ApiModelProperty(notes = "Sale's price")
	@Column(name = "precio")
	@Min(0)
	protected double precio;
	
	@ApiModelProperty(notes = "Sale's final price")
	@Column(name = "preciofinal")
	protected double preciofinal;
	
	@ApiModelProperty(notes = "Sale's buyer")
	@Column(name = "comprador")
	protected String comprador;
	
	@ApiModelProperty(notes = "Sale's payment day")
	@Column(name = "fechapago")
	protected Date fechapago;
	
	@ApiModelProperty(notes = "Sale's archive")
	@Column(name = "tienearchivo")
	protected int tienearchivo;
	
	@ApiModelProperty(notes = "If a sale is active or not")
	@Column(name = "activa")
	protected int activa;
	
	@ApiModelProperty(notes = "If a sale is a normal sale or an auction")
	@Column(name = "es_subasta")
	protected int  es_subasta;
	
	@ApiModelProperty(notes = "Sale's city")
	@Column(name = "ciudad")
	protected String ciudad;
	
	@ApiModelProperty(notes = "Sale's province")
	@Column(name = "provincia")
	protected String provincia;
	
	@ApiModelProperty(notes = "Sale's category")
	@Column(name = "categoria")
	protected String categoria;
	
	@ApiModelProperty(notes = "Sale's latitud")
	@Column(name="latitud")
	protected Float latitud;
	
	@ApiModelProperty(notes = "Sale's longitud")
	@Column(name="longitud")
	protected Float longitud;
	
	@Transient
	protected Float distancia;
	
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
		this.distancia=(float)0;
		this.longitud=(float)-9999;
		this.latitud=(float)-9999;
	}
	
	public venta() {
		this.distancia=(float)999999;
		this.longitud=(float)-9999;
		this.latitud=(float)-9999;
	}
	
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

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Float getLatitud() {
		return latitud;
	}

	public void setLatitud(float latitud) {
		this.latitud = latitud;
	}

	public Float getLongitud() {
		return longitud;
	}

	public void setLongitud(float longitud) {
		this.longitud = longitud;
	}

	public float getDistancia() {
		return distancia;
	}

	public void setDistancia(float distancia) {
		this.distancia = distancia;
	}
	
	@Override
    public int compareTo(venta v) {
        return this.distancia.compareTo(v.getDistancia());
    }
}
