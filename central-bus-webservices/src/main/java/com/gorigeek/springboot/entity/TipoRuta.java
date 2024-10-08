package com.gorigeek.springboot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "c_tiporuta")
public class TipoRuta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idc_tiporuta")
    private Long idc_tiporuta;
	
	@Column(name = "descripcion")
	private String descripcion;

    public TipoRuta() {
        super();
    }

    public TipoRuta(Long idc_tiporuta, String descripcion) {
        super();
        this.idc_tiporuta = idc_tiporuta;
        this.descripcion = descripcion;
    }

    public Long getIdc_tiporuta() {
        return idc_tiporuta;
    }

    public void setIdc_tiporuta(Long idc_tiporuta) {
        this.idc_tiporuta = idc_tiporuta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
