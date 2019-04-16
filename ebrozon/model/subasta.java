package com.ebrozon.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.ebrozon.model.venta;

@Entity
@Table(name = "subasta")
@PrimaryKeyJoinColumn(name = "nventa")
//@IdClass(venta.class)
@XmlRootElement
public class subasta extends venta{

	protected static final long serialVersionUID = 1L;
	
	private Date fechafin;
	
	@Column(name = "precioinicial")
	@Min(0)
	private double precioinicial;
	
	@Column(name = "pujaactual")
	@Min(0)
	private double pujaactual;
	
	public subasta() {}
	
	public subasta(
			@Size(min = 3, max = 30, message = "El nombre tiene que tener entre 3 y 30 caracteres") @Pattern(regexp = "[A-z,0-9,_,-]+", message = "El nombre solo puede tener letras mayúsculas o minúsculas sin acentuar, números, y los caracteres _ y -") String usuario,
			@Size(min = 3, max = 100, message = "El nombre del producto tiene que tener entre 3 y 100 caracteres") String producto,
			@Size(min = 10, message = "La descripción del producto tiene que tener mínimo 10 caracteres") String descripcion,
			@Min(0) double precio, int tienearchivo, int activa, String ci, Date fechafin, @Min(0) double precioinicial,
			@Min(0) double pujaactual) {
		super(usuario, producto, descripcion, precio, tienearchivo, activa, ci);
		this.fechafin = fechafin;
		this.precioinicial = precioinicial;
		this.pujaactual = pujaactual;
		this.es_subasta = 1;
	}

	public Date getFechafin() {
		return fechafin;
	}

	public void setFechafin(Date fechafin) {
		this.fechafin = fechafin;
	}

	public double getPrecioinicial() {
		return precioinicial;
	}

	public void setPrecioinicial(double precioinicial) {
		this.precioinicial = precioinicial;
	}

	public double getPujaactual() {
		return pujaactual;
	}

	public void setPujaactual(double pujaactual) {
		this.pujaactual = pujaactual;
	}
	
	
}
