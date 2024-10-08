package com.gorigeek.springboot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "c_tipousuario")
public class TipoUsuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idc_tipousuario")
	private int idc_tipousuario;
	
	@Column(name = "descripcion")
	private String descripcion;

	public TipoUsuario() {
	    
	}

    public TipoUsuario(int idc_tipousuario, String descripcion) {
        super();
        this.idc_tipousuario = idc_tipousuario;
        this.descripcion = descripcion;
    }

    public int getIdc_tipousuario() {
        return idc_tipousuario;
    }

    public void setIdc_tipousuario(int idc_tipousuario) {
        this.idc_tipousuario = idc_tipousuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
	
}
