package com.gorigeek.springboot.entity;

import javax.persistence.*;


@Entity
@Table(name = "c_ciudades")
public class Ciudad {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idc_ciudades;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	//@Column(name = "c_estados_idc_estados")//se repite en la bd c1
	//private Long idEstado;
	
	@Column(name = "c_estados")
    private Long idEstado;
	
	public Ciudad(){}

	public Ciudad(Long idc_ciudades, String descripcion, Long idEstado) {
		super();
		this.idc_ciudades = idc_ciudades;
		this.descripcion = descripcion;
		this.idEstado = idEstado;
	}

	public Long getIdc_ciudades() {
		return idc_ciudades;
	}

	public void setIdc_ciudades(Long idc_ciudades) {
		this.idc_ciudades = idc_ciudades;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Long getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Long idEstado) {
		this.idEstado = idEstado;
	}
	
	
	
}
