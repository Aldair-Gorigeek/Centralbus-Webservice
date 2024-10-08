package com.gorigeek.springboot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "c_tipotransporte")
public class TipoTransporte {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idc_tipotransporte")
	private Long idTipoTransporte;
	
	@Column(name = "descripcion", insertable = false, updatable = false)
	private String descripcion;
	
	@Column(name = "icono")
    private String icono;
	
	public TipoTransporte() {
		
	}

    public TipoTransporte(Long idTipoTransporte, String descripcion, String icono) {
        super();
        this.idTipoTransporte = idTipoTransporte;
        this.descripcion = descripcion;
        this.icono = icono;
    }

    public Long getIdTipoTransporte() {
        return idTipoTransporte;
    }

    public void setIdTipoTransporte(Long idTipoTransporte) {
        this.idTipoTransporte = idTipoTransporte;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }
	
	
	
	
}
