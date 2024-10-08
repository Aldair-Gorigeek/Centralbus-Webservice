package com.gorigeek.springboot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "c_tipoboleto")
public class TipoBoleto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idc_tipoBoleto")
    private Long idc_tipoBoleto;
	
	@Column(name = "descripcion")
	private String descripcion;

    public TipoBoleto() {
        super();
    }

    public TipoBoleto(Long idc_tipoBoleto, String descripcion) {
        super();
        this.idc_tipoBoleto = idc_tipoBoleto;
        this.descripcion = descripcion;
    }

    public Long getIdc_tipoBoleto() {
        return idc_tipoBoleto;
    }

    public void setIdc_tipoBoleto(Long idc_tipoBoleto) {
        this.idc_tipoBoleto = idc_tipoBoleto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
