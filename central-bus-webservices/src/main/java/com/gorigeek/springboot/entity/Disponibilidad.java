package com.gorigeek.springboot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="c_disponibilidad")
public class Disponibilidad {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idc_disponibilidad;
	
	@Column(name="descripcion")
	private String descripcion;
	
	public Disponibilidad() {
		
	}

	public Disponibilidad(Long idc_disponibilidad, String descripcion) {
		super();
		this.idc_disponibilidad = idc_disponibilidad;
		this.descripcion = descripcion;
	}

	public Long getIdc_disponibilidad() {
		return idc_disponibilidad;
	}

	public void setIdc_disponibilidad(Long idc_statusdisponibilidad) {
		this.idc_disponibilidad = idc_statusdisponibilidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	

}
