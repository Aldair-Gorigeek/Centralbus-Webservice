package com.gorigeek.springboot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "c_statuscuenta")
public class StatusCuenta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idc_statusCuenta")
	private int idc_statusCuenta;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	public StatusCuenta() {
	}

    public StatusCuenta(int idc_statusCuenta, String descripcion) {
        super();
        this.idc_statusCuenta = idc_statusCuenta;
        this.descripcion = descripcion;
    }

    public int getIdc_statusCuenta() {
        return idc_statusCuenta;
    }

    public void setIdc_statusCuenta(int idc_statusCuenta) {
        this.idc_statusCuenta = idc_statusCuenta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
