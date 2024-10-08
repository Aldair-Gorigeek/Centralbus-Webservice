package com.gorigeek.springboot.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "c_tipo_tarjeta")
public class TipoTarjeta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "idc_tipo_tarjeta")
    private Long idcTipoTarjeta;
    
    @Column(name = "descripcion")
    private String descripcion;

    public TipoTarjeta() {
        super();
    }

    public TipoTarjeta(Long idcTipoTarjeta, String descripcion) {
        super();
        this.idcTipoTarjeta = idcTipoTarjeta;
        this.descripcion = descripcion;
    }

    public Long getIdcTipoTarjeta() {
        return idcTipoTarjeta;
    }

    public void setIdcTipoTarjeta(Long idcTipoTarjeta) {
        this.idcTipoTarjeta = idcTipoTarjeta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
    
    

}
