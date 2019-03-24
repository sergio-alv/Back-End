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

	@Id
	@Column(name = "usuario")
	private String usuario;
	
	@Id
	@Column(name = "fechainicio")
	private Date fechainicio;
	
	
}
