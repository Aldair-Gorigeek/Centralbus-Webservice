package com.gorigeek.springboot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="c_estados")
public class Estado {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idc_estados;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	public Estado() {	}



	public Estado(String descripcion) {
		super();
		this.descripcion = descripcion;
	}



	public Long getIdc_estados() {
		return idc_estados;
	}

	public void setIdc_estados(Long idc_estados) {
		this.idc_estados = idc_estados;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
